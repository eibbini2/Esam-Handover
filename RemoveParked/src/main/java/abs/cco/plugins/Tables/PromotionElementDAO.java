package abs.cco.plugins.Tables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.util.StringUtils;
import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.util.logging.Logger;
import abs.cco.plugins.models.PromotionElementModel;

public class PromotionElementDAO {

	private static final Logger logger = Logger.getLogger(PromotionElementDAO.class);

	private static final String TABLE_NAME = "PromotionElements";

	private static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + "PromotionElementID int NOT NULL Primary Key," + "PromotionID int NOT NULL," + "ItemLevelTypeId int NOT NULL," + "ItemLevelReference VARCHAR(32672) NOT NULL," + "ItemUOMID VARCHAR(32672),"
			+ "QtyPoint decimal(18, 3)," + "ValuePoint decimal(18, 3)," + "IncentiveValue decimal(18, 3))";

	private static final String QUERY_INSERT_ROW = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?)";

	private static final String QUERY_UPDATE_ROW = "UPDATE " + TABLE_NAME + " SET PromotionID =?2, ItemLevelTypeId = ?3, ItemLevelReference = ?4,ItemUOMID =?5 ,QtyPoint = ?6," + "ValuePoint = ?7,IncentiveValue = ?8 WHERE PromotionElementID = ?1";

	private static final String QUERY_FIND_ALL = "SELECT * FROM " + TABLE_NAME;

	private static final String QUERY_FIND_ONE = QUERY_FIND_ALL + " WHERE PromotionElementID = ?";

	private static final String QUERY_FIND_ALL_BY_PARENT = QUERY_FIND_ALL + " WHERE PromotionID = ?";

	private static final String QUERY_DELETE_ONE = "DELETE FROM " + TABLE_NAME + " WHERE PromotionElementID = ?";
	private static final String QUERY_DELETE_ONE_BY_PARENT = "DELETE FROM " + TABLE_NAME + " WHERE PromotionID = ?";

	private static final String QUERY_DELETE_ALL = "DELETE FROM " + TABLE_NAME;

	private static PromotionElementDAO instance;

	public static synchronized PromotionElementDAO getInstance() {
		if (instance == null) {
			instance = new PromotionElementDAO();
		}
		return instance;
	}

	private PromotionElementDAO() {

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

	private void save(PromotionElementModel dto, boolean isAlreadyInDB) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = isAlreadyInDB ? QUERY_UPDATE_ROW : QUERY_INSERT_ROW;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();

			Query q = em.createNativeQuery(query);
			q.setParameter(1, dto.getPromotionElementID());
			q.setParameter(2, dto.getPromotionID());
			q.setParameter(3, dto.getItemLevelTypeId());
			q.setParameter(4, dto.getItemLevelReference());
			q.setParameter(5, dto.getItemUOMID());
			q.setParameter(6, dto.getQtyPoint());
			q.setParameter(7, dto.getValuePoint());
			q.setParameter(8, dto.getIncentiveValue());
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

	private PromotionElementModel MapPromotionElementModel(Object[] resultRow) {

		PromotionElementModel m = new PromotionElementModel();
		m.setPromotionElementID((Integer) resultRow[0]);
		m.setPromotionID((Integer) resultRow[1]);
		m.setItemLevelTypeId((Integer) resultRow[2]);
		m.setItemLevelReference((String) resultRow[3]);
		m.setItemUOMID((String) resultRow[4]);
		m.setQtyPoint((BigDecimal) resultRow[5]);
		m.setValuePoint((BigDecimal) resultRow[6]);
		m.setIncentiveValue((BigDecimal) resultRow[7]);
		return m;
	}

	public List<PromotionElementModel> findAll(int promotionId) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		ArrayList<PromotionElementModel> resultList = new ArrayList<PromotionElementModel>();

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
				resultList.add(MapPromotionElementModel(resultRow));
			}

		} catch (Exception e) {
			logger.info("Error while getting results from table " + TABLE_NAME);

		} finally {
			session.closeDBSession();

		}
		return resultList;
	}

	public PromotionElementModel findOne(int promotionElementID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		PromotionElementModel PromotionElementModel = new PromotionElementModel();

		try {

			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(QUERY_FIND_ONE);
			q.setParameter(1, promotionElementID);

			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();

			if (results.isEmpty()) {
				return PromotionElementModel;
			}
			PromotionElementModel = MapPromotionElementModel(results.get(0));

		} catch (Exception e) {
			logger.info("Error while getting " + promotionElementID + " from table " + TABLE_NAME);
		} finally {
			session.closeDBSession();
		}
		return PromotionElementModel;
	}

	public void save(PromotionElementModel dto) {

		int promotionId = dto.getPromotionID();
		PromotionElementModel dtoInDb = this.findOne(promotionId);
		boolean isAlreadyInDb = StringUtils.pathEquals(Integer.toString(promotionId), Integer.toString(dtoInDb.getPromotionID()));
		logger.info("Trying to save PromotionElement" + dto.getPromotionElementID() + " and it was in db is = " + isAlreadyInDb);
		this.save(dto, isAlreadyInDb);

	}

	public void Delete(int promotionElementID) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		String query = QUERY_DELETE_ONE;

		try {
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery(query);
			q.setParameter(1, promotionElementID);
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
