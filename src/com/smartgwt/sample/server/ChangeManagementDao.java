package com.smartgwt.sample.server;

import com.isomorphic.datasource.DSRequest;
import com.isomorphic.datasource.DSResponse;
import com.isomorphic.log.Logger;
import com.isomorphic.util.DataTools;
import com.isomorphic.util.ErrorReport;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChangeManagementDao {

    Logger log = new Logger(com.smartgwt.sample.server.ChangeManagementDao.class.getName());

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public DSResponse fetch(DSRequest dsRequest)
            throws Exception {
        log.info("procesing DMI fetch operation");
        DSResponse dsResponse = new DSResponse();

        Session hibernateSession = sessionFactory.getCurrentSession();

        String changeName = (String) dsRequest.getFieldValue("changeName");

        long startRow = (int) dsRequest.getStartRow();
        long endRow = (int) dsRequest.getEndRow();

        Criteria criteria = hibernateSession.createCriteria(ChangeManagement.class);
        Criterion changeNameRestriction = null;
        if (changeName != null) {
            changeNameRestriction = Restrictions.like("changeName", changeName, MatchMode.ANYWHERE);
            criteria.add(changeNameRestriction);
        }

        criteria.setProjection(Projections.rowCount());
        Object rowCount = criteria.uniqueResult();
        long totalRows = 0;
        if (rowCount instanceof Integer) {
            totalRows = ((Integer) rowCount).intValue();
        } else if (rowCount instanceof Long) {
            totalRows = ((Long) rowCount).longValue();
        }
        endRow = Math.min(endRow, totalRows);

        criteria = hibernateSession.createCriteria(ChangeManagement.class);
        if (changeName != null) criteria.add(changeNameRestriction);

        criteria.setFirstResult((int) startRow);
        criteria.setMaxResults((int) (endRow - startRow));
        List matchingItems = criteria.list();

        dsResponse.setData(matchingItems);
        dsResponse.setStartRow(startRow);
        dsResponse.setEndRow(endRow);
        dsResponse.setTotalRows(totalRows);

        return dsResponse;
    }

    public DSResponse add(DSRequest dsRequest, ChangeManagement change)
            throws Exception {
        log.info("procesing DMI add operation");

        DSResponse dsResponse = new DSResponse();

        // perform validation
        ErrorReport errorReport = dsRequest.getDataSource().validate(DataTools.getProperties(change), false);
        if (errorReport != null) {
            dsResponse.setStatus(DSResponse.STATUS_VALIDATION_ERROR);
            dsResponse.setErrorReport(errorReport);
            System.out.println("Errors: " + DataTools.prettyPrint(errorReport));
            return dsResponse;
        }


        Session hibernateSession = sessionFactory.getCurrentSession();
        hibernateSession.saveOrUpdate(change);
        dsResponse.setData(change);
        return dsResponse;
    }


    public DSResponse update(DSRequest dsRequest, Map newValues)
            throws Exception {
        log.info("procesing DMI update operation");

        DSResponse dsResponse = new DSResponse();

        // perform validation
        ErrorReport errorReport = dsRequest.getDataSource().validate(newValues, false);
        if (errorReport != null) {
            dsResponse.setStatus(DSResponse.STATUS_VALIDATION_ERROR);
            dsResponse.setErrorReport(errorReport);
            System.out.println("Errors: " + DataTools.prettyPrint(errorReport));
            return dsResponse;
        }

        // primary key
        Serializable id = (Serializable) dsRequest.getFieldValue("changeId");

        Session hibernateSession = sessionFactory.getCurrentSession();
        ChangeManagement change = (ChangeManagement) hibernateSession.get(ChangeManagement.class, id);

        log.warn("fetched change: " + DataTools.prettyPrint(change));

        // apply new values to the as-saved bean
        DataTools.setProperties(newValues, change);

        log.warn("Saving record: " + DataTools.prettyPrint(change));

        // persist
        hibernateSession.saveOrUpdate(change);
        dsResponse.setData(change);
        return dsResponse;
    }


    public ChangeManagement remove(ChangeManagement change)
            throws Exception {
        log.info("procesing DMI remove operation");

        Session hibernateSession = sessionFactory.getCurrentSession();
        hibernateSession.delete(change);

        return change;
    }


}

