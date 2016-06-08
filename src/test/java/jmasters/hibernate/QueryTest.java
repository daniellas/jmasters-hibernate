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
public class QueryTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void initOnce() {
        emf = Persistence.createEntityManagerFactory("jmasters");
        em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Teacher teacher;

        tx.begin();

        teacher = new Teacher();
        teacher.setFirstName("Jan");
        teacher.setLastName("Kowalski");
        teacher.setNick("Jasio");
        teacher.setAge(30);
        em.persist(teacher);

        teacher = new Teacher();
        teacher.setFirstName("Tomasz");
        teacher.setLastName("Nowak");
        teacher.setNick("Nowaczek");
        teacher.setAge(35);
        em.persist(teacher);

        teacher = new Teacher();
        teacher.setFirstName("Grzegorz");
        teacher.setLastName("Brzęczyszczykiewicz");
        teacher.setNick("Bzyk");
        teacher.setAge(50);
        em.persist(teacher);

        tx.commit();
    }

    @Test
    public void findByFirstNameShouldReturnOneRow() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Jan'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(1, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByFirstNameFirstLetterJShouldReturnOneRow() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName like 'J%'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(1, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByFirstNameLastLetterZShouldReturnTwoRows() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName like '%z'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(2, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByFirstNameJanOrTomaszShouldReturnTwoRows() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Jan' or t.firstName='Tomasz'",
                Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(2, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByFirstNameLastNameShouldReturnOneRow() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Jan' and t.lastName='Kowalski'",
                Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(1, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByAgeOver30ShouldReturnTwoRows() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.age>30", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(2, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findByAgeBelowEqual35ShouldReturnTwoRows() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.age<=35", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(2, teachers.size());
        System.out.println(teachers);
    }

    @Test
    public void findAgregatesShouldReturnValues() {
        TypedQuery<Object[]> query = em.createQuery(
                "select MIN(t.age),MAX(t.age),AVG(t.age),COUNT(*) from jmasters.hibernate.entity.Teacher t",
                Object[].class);
        List<Object[]> teachers = query.getResultList();

        Assert.assertEquals(1, teachers.size());
        Object[] row = teachers.iterator().next();
        System.out.println(row[0]);
        System.out.println(row[1]);
        System.out.println(row[2]);
        System.out.println(row[3]);
    }

}
