package abs.cco.plugins.Tables;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.util.StringUtils;
import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.util.logging.Logger;
import abs.cco.plugins.models.PromotionSchemeModel;

public class PromotionSchemeDAO {

	private static final Logger logger = Logger.getLogger(PromotionSchemeDAO.class);
	private static final String TABLE_NAME = "PromotionSchemes";
	private static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
			+ "PromotionID int NOT NULL PRIMARY KEY," + "SchemeBaseType int NOT NULL,"
			+ "CustomerLevelTypeId int NOT NULL," + "POSLevelTypeId int NOT NULL," + "POSs VARCHAR(32672),"
			+ "StoreID VARCHAR(32672)," + "CustomerGroupIDs VARCHAR(32672)," + "CustomerIDs VARCHAR(32672),"
			+ "PromotionAppliedID int NOT NULL," + "DiscountTypeID int NOT NULL," + "Description VARCHAR(200) NOT NULL,"
			+ "IncentiveID int NOT NULL)";

	private static final String QUERY_INSERT_ROW = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String QUERY_UPDATE_ROW = "UPDATE " + TABLE_NAME
			+ " SET SchemeBaseType =?2, CustomerLevelTypeId = ?3, POSLevelTypeId = ?4,POSs = ?5,"
			+ "StoreID = ?6,CustomerGroupIDs = ?7,CustomerIDs = ?8,PromotionAppliedID = ?9,DiscountTypeID = ?10,Description = ?11,IncentiveID = ?12 WHERE PromotionID = ?1";

	private static final String QUERY_FIND_ALL = "SELECT * FROM " + TABLE_NAME;
	private static final String QUERY_FIND_ONE = QUERY_FIND_ALL + " WHERE PromotionID = ?";
	private static final String QUERY_DELETE_ONE = "DELETE FROM " + TABLE_NAME + " WHERE PromotionID = ?";
	private static final String QUERY_DELETE_ALL = "DELETE FROM " + TABLE_NAME;
	private static PromotionSchemeDAO instance;

	public static synchronized PromotionSchemeDAO getInstance() {
		if (instance == null) {
			instance = new PromotionSchemeDAO();
		}
		return instance;
	}

	private PromotionSchemeDAO() {

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

	private void save(PromotionSchemeModel dto, boolean isAlreadyInDB) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = isAlreadyInDB ? QUERY_UPDATE_ROW : QUERY_INSERT_ROW;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(query);
			q.setParameter(1, dto.getPromotionID());
			q.setParameter(2, dto.getSchemeBaseType());
			q.setParameter(3, dto.getCustomerLevelTypeId());
			q.setParameter(4, dto.getPOSLevelTypeId());
			q.setParameter(5, dto.getPOSs());
			q.setParameter(6, dto.getStoreID());
			q.setParameter(7, dto.getCustomerGroupIDs());
			q.setParameter(8, dto.getCustomerIDs());
			q.setParameter(9, dto.getPromotionAppliedID());
			q.setParameter(10, dto.getDiscountTypeID());
			q.setParameter(11, dto.getDescription());
			q.setParameter(12, dto.getIncentiveID());
			q.executeUpdate();
			session.commitTransaction();

		} catch (Exception e) {
			session.rollbackDBSession();
			logger.info("Could not save table " + TABLE_NAME);
			logger.info(e.getLocalizedMessage());
		} finally {
			session.closeDBSession();
		}
	}

	private PromotionSchemeModel MapPromotionSchemeModel(Object[] resultRow) {

		PromotionSchemeModel m = new PromotionSchemeModel();
		m.setPromotionID((Integer) resultRow[0]);
		m.setSchemeBaseType((Integer) resultRow[1]);
		m.setCustomerLevelTypeId((Integer) resultRow[2]);
		m.setPOSLevelTypeId((Integer) resultRow[3]);
		m.setPOSs((String) resultRow[4]);
		m.setStoreID((String) resultRow[5]);
		m.setCustomerGroupIDs((String) resultRow[6]);
		m.setCustomerIDs((String) resultRow[7]);
		m.setPromotionAppliedID((Integer) resultRow[8]);
		m.setDiscountTypeID((Integer) resultRow[9]);
		m.setDescription((String) resultRow[10]);
		m.setIncentiveID((Integer) resultRow[11]);
		return m;
	}

	public List<PromotionSchemeModel> findAll() {
		CDBSession session = CDBSessionFactory.instance.createSession();
		ArrayList<PromotionSchemeModel> resultList = new ArrayList<PromotionSchemeModel>();

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(QUERY_FIND_ALL);
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();

			if (results.isEmpty()) {
				return resultList;
			}

			for (Object[] resultRow : results) {
				resultList.add(MapPromotionSchemeModel(resultRow));
			}

		} catch (Exception e) {
			logger.info("Error while getting results from table " + TABLE_NAME);

		} finally {
			session.closeDBSession();

		}
		return resultList;
	}

	public PromotionSchemeModel findOne(int promotionId) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		PromotionSchemeModel PromotionSchemeModel = new PromotionSchemeModel();
		try {
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(QUERY_FIND_ONE);
			q.setParameter(1, promotionId);
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();

			if (results.isEmpty()) {
				return PromotionSchemeModel;
			}
			PromotionSchemeModel = MapPromotionSchemeModel(results.get(0));

		} catch (Exception e) {
			logger.info("Error while getting " + promotionId + " from table " + TABLE_NAME);
		} finally {
			session.closeDBSession();
		}
		return PromotionSchemeModel;
	}

	public void save(PromotionSchemeModel dto) {

		int promotionId = dto.getPromotionID();
		PromotionSchemeModel dtoInDb = this.findOne(promotionId);
		boolean isAlreadyInDb = StringUtils.pathEquals(Integer.toString(promotionId),
				Integer.toString(dtoInDb.getPromotionID()));
		logger.info("Trying to save Promotion " + dto.getPromotionID() + " and it was in db is = " + isAlreadyInDb);
		this.save(dto, isAlreadyInDb);

	}

	public void Delete(int promotionID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = QUERY_DELETE_ONE;

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
