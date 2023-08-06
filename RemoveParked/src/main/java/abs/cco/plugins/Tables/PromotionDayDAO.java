package abs.cco.plugins.Tables;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.util.StringUtils;
import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.util.logging.Logger;
import abs.cco.plugins.models.PromotionDaysModel;

public class PromotionDayDAO {

	private static final Logger logger = Logger.getLogger(PromotionDayDAO.class);

	private static final String TABLE_NAME = "PromotionDays";

	private static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
			+ "PromotionDayID int NOT NULL Primary Key," + "PromotionID int NOT NULL," + "WeekDayID int NOT NULL,"
			+ "StartTime VARCHAR(30) NOT NULL," + "EndTime VARCHAR(30) NOT NULL)";

	private static final String QUERY_INSERT_ROW = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?)";
	private static final String QUERY_UPDATE_ROW = "UPDATE " + TABLE_NAME
			+ " SET PromotionID =?2, WeekDayID = ?3, StartTime = ?4,EndTime =?5 WHERE PromotionDayID = ?1";
	private static final String QUERY_FIND_ALL = "SELECT * FROM " + TABLE_NAME;
	private static final String QUERY_FIND_ONE = QUERY_FIND_ALL + " WHERE PromotionDayID = ?";
	private static final String QUERY_FIND_ALL_BY_PARENT = QUERY_FIND_ALL + " WHERE PromotionID = ?";
	private static final String QUERY_DELETE_ONE = "DELETE FROM " + TABLE_NAME + " WHERE PromotionDayID = ?";
	private static final String QUERY_DELETE_ONE_BY_PARENT = "DELETE FROM " + TABLE_NAME + " WHERE PromotionID = ?";
	private static final String QUERY_DELETE_ALL = "DELETE FROM " + TABLE_NAME;
	private static PromotionDayDAO instance;

	public static synchronized PromotionDayDAO getInstance() {
		if (instance == null) {
			instance = new PromotionDayDAO();
		}
		return instance;
	}

	private PromotionDayDAO() {

	}

	public void setupTable() {
		CDBSession session = CDBSessionFactory.instance.createSession();
		try {
			session.beginTransaction();

			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(QUERY_CREATE_TABLE);
			q.executeUpdate();

			session.commitTransaction();
			logger.info("Created Table" + TABLE_NAME);
		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Table " + TABLE_NAME + " already exists or another db related error occured");
		} finally {
			session.closeDBSession();
		}
	}

	private void save(PromotionDaysModel dto, boolean isAlreadyInDB) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = isAlreadyInDB ? QUERY_UPDATE_ROW : QUERY_INSERT_ROW;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(query);
			q.setParameter(1, dto.getPromotionDayID());
			q.setParameter(2, dto.getPromotionID());
			q.setParameter(3, dto.getWeekDayID());
			q.setParameter(4, dto.getStartTime());
			q.setParameter(5, dto.getEndTime());
			q.executeUpdate();
			session.commitTransaction();

		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Could not create table " + TABLE_NAME);
			logger.info(e.getLocalizedMessage());
		} finally {
			session.closeDBSession();
		}
	}

	private PromotionDaysModel MapPromotionDaysModel(Object[] resultRow) {

		PromotionDaysModel m = new PromotionDaysModel();
		m.setPromotionDayID((Integer) resultRow[0]);
		m.setPromotionID((Integer) resultRow[1]);
		m.setWeekDayID((Integer) resultRow[2]);
		m.setStartTime((String) resultRow[3]);
		m.setEndTime((String) resultRow[4]);
		return m;
	}

	public List<PromotionDaysModel> findAll(int promotionId) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		ArrayList<PromotionDaysModel> resultList = new ArrayList<PromotionDaysModel>();

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(QUERY_FIND_ALL_BY_PARENT);
			q.setParameter(1, promotionId);
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();

			if (results.isEmpty()) {
				return resultList;
			}

			for (Object[] resultRow : results) {
				resultList.add(MapPromotionDaysModel(resultRow));
			}

		} catch (Exception e) {
			logger.info("Error while getting results from table " + TABLE_NAME);

		} finally {
			session.closeDBSession();

		}
		return resultList;
	}

	public PromotionDaysModel findOne(int PromotionDayID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		PromotionDaysModel PromotionDaysModel = new PromotionDaysModel();

		try {

			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(QUERY_FIND_ONE);
			q.setParameter(1, PromotionDayID);

			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();

			if (results.isEmpty()) {
				return PromotionDaysModel;
			}
			PromotionDaysModel = MapPromotionDaysModel(results.get(0));

		} catch (Exception e) {
			logger.info("Error while getting " + PromotionDayID + " from table " + TABLE_NAME);
		} finally {
			session.closeDBSession();
		}
		return PromotionDaysModel;
	}

	public void save(PromotionDaysModel dto) {

		int promotionId = dto.getPromotionID();
		PromotionDaysModel dtoInDb = this.findOne(promotionId);
		boolean isAlreadyInDb = StringUtils.pathEquals(Integer.toString(promotionId),
				Integer.toString(dtoInDb.getPromotionID()));
		logger.info("Trying to save PromotionDay" + dto.getPromotionDayID() + " and it was in db is = " + isAlreadyInDb);
		this.save(dto, isAlreadyInDb);

	}

	public void Delete(int PromotionDayID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = QUERY_DELETE_ONE;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(query);
			q.setParameter(1, PromotionDayID);
			q.executeUpdate();
			session.commitTransaction();

		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Could not delete all table " + TABLE_NAME);
			logger.info(e.getLocalizedMessage());
		} finally {
			session.closeDBSession();
		}
	}

	public void DeleteAll(int promotionID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = QUERY_DELETE_ONE_BY_PARENT;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(query);
			q.setParameter(1, promotionID);
			q.executeUpdate();
			session.commitTransaction();

		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Could not delete all table " + TABLE_NAME);
			logger.info(e.getLocalizedMessage());
		} finally {
			session.closeDBSession();
		}
	}

	public void DeleteAll() {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = QUERY_DELETE_ALL;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(query);
			q.executeUpdate();
			session.commitTransaction();

		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Could not delete all table " + TABLE_NAME);
			logger.info(e.getLocalizedMessage());
		} finally {
			session.closeDBSession();
		}
	}

}
