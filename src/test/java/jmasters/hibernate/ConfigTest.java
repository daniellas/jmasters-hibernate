package jmasters.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void testConfiguration() {
        EntityManagerFactory em = Persistence.createEntityManagerFactory("jmasters");

        Assert.assertNotNull(em);
    }
}
