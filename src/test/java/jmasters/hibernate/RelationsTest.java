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
import jmasters.hibernate.entity.Pupil;
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

        tx.begin();

        Course math = new Course();
        math.setName("Matematyka");

        Course english = new Course();
        english.setName("J. angielski");

        Teacher jan = new Teacher();
        jan.setFirstName("Jan");
        jan.setLastName("Kowalski");
        jan.setNick("Jasio");
        jan.setAge(30);
        jan.setCourse(math);

        Teacher tomasz = new Teacher();
        tomasz.setFirstName("Tomasz");
        tomasz.setLastName("Nowak");
        tomasz.setNick("Nowaczek");
        tomasz.setAge(35);
        tomasz.setCourse(math);

        Teacher grzegorz = new Teacher();
        grzegorz.setFirstName("Grzegorz");
        grzegorz.setLastName("Brzęczyszczykiewicz");
        grzegorz.setNick("Bzyk");
        grzegorz.setAge(50);
        grzegorz.setCourse(english);

        math.getTeachers().add(jan);
        math.getTeachers().add(tomasz);
        em.persist(math);

        english.getTeachers().add(grzegorz);
        em.persist(english);

        Pupil marcin = new Pupil();
        marcin.setFirstName("Marcin");
        marcin.setLastName("Marcinkiewicz");
        em.persist(marcin);

        Pupil michal = new Pupil();
        michal.setFirstName("Michał");
        michal.setLastName("Michalski");
        em.persist(michal);

        jan.getPupils().add(marcin);
        jan.getPupils().add(michal);
        em.merge(jan);

        michal.getTeachers().add(jan);
        em.merge(michal);
        marcin.getTeachers().add(jan);
        em.merge(marcin);

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

    @Test
    public void coursesShouldHaveValidTeachers() {
        TypedQuery<Course> query = em.createQuery(
                "select c from jmasters.hibernate.entity.Course c", Course.class);
        List<Course> courses = query.getResultList();
        Course course = courses.get(0);

        Assert.assertEquals("Jan", course.getTeachers().get(0).getFirstName());
        Assert.assertEquals("Tomasz", course.getTeachers().get(1).getFirstName());
        System.out.println(course.getTeachers());

        course = courses.get(1);
        Assert.assertEquals("Grzegorz", course.getTeachers().get(0).getFirstName());
        System.out.println(course.getTeachers());

    }

    @Test
    public void teacherShouldHasTwoPupils() {
        TypedQuery<Teacher> query = em.createQuery(
                "select t from jmasters.hibernate.entity.Teacher t where t.firstName='Jan'", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        Assert.assertEquals(2, teachers.iterator().next().getPupils().size());
        System.out.println(teachers.iterator().next().getPupils());
    }

    @Test
    public void michalShuldHasOneTeacher() {
        TypedQuery<Pupil> query = em.createQuery(
                "select p from jmasters.hibernate.entity.Pupil p where p.firstName='Michał'", Pupil.class);
        List<Pupil> pupils = query.getResultList();

        Assert.assertEquals(1, pupils.iterator().next().getTeachers().size());
        System.out.println(pupils.iterator().next().getTeachers());
    }

}
