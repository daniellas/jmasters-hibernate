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

import jmasters.hibernate.entity.Course;
import jmasters.hibernate.entity.Teacher;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RelationsTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void initOnce() {
        emf = Persistence.createEntityManagerFactory("jmasters");
        em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Teacher teacher;

        Course math = new Course();

        math.setName("Matematyka");

        Course english = new Course();

        english.setName("J. angielski");

        tx.begin();

        em.persist(math);
        em.persist(english);

        teacher = new Teacher();
        teacher.setFirstName("Jan");
        teacher.setLastName("Kowalski");
        teacher.setNick("Jasio");
        teacher.setAge(30);
        teacher.setCourse(math);
        em.persist(teacher);

        teacher = new Teacher();
        teacher.setFirstName("Tomasz");
        teacher.setLastName("Nowak");
        teacher.setNick("Nowaczek");
        teacher.setAge(35);
        teacher.setCourse(math);
        em.persist(teacher);

        teacher = new Teacher();
        teacher.setFirstName("Grzegorz");
        teacher.setLastName("Brzęczyszczykiewicz");
        teacher.setNick("Bzyk");
        teacher.setAge(50);
        teacher.setCourse(english);
        em.persist(teacher);

        tx.commit();
    }

    @Test
    public void mathTeacherShouldHasMathCourseSet() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Jan'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals("Matematyka", teachers.iterator().next().getCourse().getName());
        System.out.println(teachers);
    }

    @Test
    public void englishTeacherShouldHasEnglishCourseSet() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Grzegorz'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals("J. angielski", teachers.iterator().next().getCourse().getName());
        System.out.println(teachers);
    }

}
