package jmasters.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import jmasters.hibernate.entity.Teacher;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    private static Long id;

    @BeforeClass
    public static void initOnce() {
        emf = Persistence.createEntityManagerFactory("jmasters");
        em = emf.createEntityManager();
    }

    @Test(expected = PersistenceException.class)
    public void test1InsertWithoutFirstLastNameShouldFail() {
        Teacher teacher = new Teacher();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            em.persist(teacher);

            tx.commit();

            Assert.assertNotNull(teacher.getId());
            id = teacher.getId();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Błąd: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void test2InsertWithFirstLastNameShouldSuccess() {
        Teacher teacher = new Teacher();

        teacher.setFirstName("Jan");
        teacher.setLastName("Kowalski");
        teacher.setNick("Jasio");

        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            em.persist(teacher);

            tx.commit();

            Assert.assertNotNull(teacher.getId());
            Assert.assertEquals("Jan", teacher.getFirstName());
            Assert.assertEquals("Kowalski", teacher.getLastName());
            Assert.assertEquals("Jasio", teacher.getNick());

            id = teacher.getId();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Test
    public void test3FindByIdShouldReturnTeacherWithValidFirstLastName() {
        Teacher teacher = em.find(Teacher.class, id);

        Assert.assertEquals("Jan", teacher.getFirstName());
        Assert.assertEquals("Kowalski", teacher.getLastName());
        Assert.assertEquals("Jasio", teacher.getNick());
    }

    @Test(expected = RollbackException.class)
    public void test4AddSameNickShouldFail() {
        Teacher teacher = new Teacher();

        teacher.setFirstName("Tomasz");
        teacher.setLastName("Nowak");
        teacher.setNick("Jasio");

        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();

            em.persist(teacher);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            System.out.println("Błąd: "+e.getCause().getCause().getCause().getMessage());
            throw e;
        }
    }
}
