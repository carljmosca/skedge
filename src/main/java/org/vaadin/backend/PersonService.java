package org.vaadin.backend;

import org.vaadin.backend.domain.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
public class PersonService {

    @PersistenceContext(unitName = "skedge-pu")
    private EntityManager entityManager;

    public void saveOrPersist(Person entity) {
        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    public void deleteEntity(Person entity) {
        if (entity.getId() > 0) {
            // reattach to remove
            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }
    }

    public List<Person> findAll() {
        CriteriaQuery<Person> cq = entityManager.getCriteriaBuilder().
                createQuery(Person.class);
        cq.select(cq.from(Person.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Person> findByName(String filter) {
        if (filter == null || filter.isEmpty()) {
            return findAll();
        }
        filter = filter.toLowerCase();
        return entityManager.createNamedQuery("Person.findByName",
                Person.class)
                .setParameter("filter", filter + "%").getResultList();
    }


}
