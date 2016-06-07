package jmasters.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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

    @Test
    public void test1InsertShouldSetId() {
        Teacher teacher = new Teacher();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(teacher);
        tx.commit();
        Assert.assertNotNull(teacher.getId());
        id = teacher.getId();
    }

    @Test
    public void test2FindByIdShouldHasSameId() {
        Teacher teacher = em.find(Teacher.class, id);

        Assert.assertEquals(id, teacher.getId());
    }

    @Test
    public void test3UpdateShouldSuccess() {
        Teacher teacher = em.find(Teacher.class, id);

        teacher.setFirstName("Jan");
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.merge(teacher);
        tx.commit();
    }

    @Test
    public void test4UpdatedShouldHasFirstName() {
        Teacher teacher = em.find(Teacher.class, id);

        Assert.assertEquals("Jan", teacher.getFirstName());
    }

    @Test
    public void test5FindAllShouldReturnOneRow() {
        TypedQuery<Teacher> query = em.createQuery("select t from jmasters.hibernate.entity.Teacher t", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(1, teachers.size());
        Assert.assertEquals(id, teachers.iterator().next().getId());
        Assert.assertEquals("Jan", teachers.iterator().next().getFirstName());
    }

}
