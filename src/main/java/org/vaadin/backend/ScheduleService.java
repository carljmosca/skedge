/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.backend;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import org.vaadin.backend.domain.ScheduleHeader;

/**
 *
 * @author moscac
 */
@Stateless
public class ScheduleService {

    @PersistenceContext(unitName = "skedge-pu")
    private EntityManager entityManager;

    public void saveOrPersist(ScheduleHeader entity) {
        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    public void deleteEntity(ScheduleHeader entity) {
        if (entity.getId() > 0) {
            // reattach to remove
            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }
    }

    public List<ScheduleHeader> findAll() {
        CriteriaQuery<ScheduleHeader> cq = entityManager.getCriteriaBuilder().
                createQuery(ScheduleHeader.class);
        cq.select(cq.from(ScheduleHeader.class));
        return entityManager.createQuery(cq).getResultList();
    }
    
}
