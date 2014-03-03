/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

/**
 *
 * @author dandrus
 */
public class TestUtils
{
    public static AppMetaData createAppMetaData()
    {
        AppMetaData app = new AppMetaData();
        app.setAuthor("author");
        app.setDescription("description");
        app.setName("name");
        app.setOrganization("organization");
        app.setVersion("2.5");
        app.addTag("tag1");
        app.addTag("tag2");
        app.addTag("tag3");

        app.setFunctionalArea("function");
        app.setMissionArea("mission");
        app.setSecurityLevel("UNCLASS");

        BundleMetaData bmd1 = new BundleMetaData();
        bmd1.setFileName("bundle1.jar");
        String sum1 = "ec0b1e4d93d5e5c4b32645e6ea9e5ac1";
        bmd1.setExpectedCheckSum(sum1);
        app.addBundle(bmd1);

        BundleMetaData bmd2 = new BundleMetaData();
        bmd2.setFileName("bundle2.jar");
        String sum2 = "244d38b161425c7e0dd4e945c2c9fb7d";
        bmd2.setExpectedCheckSum(sum2);
        app.addBundle(bmd2);

        BundleMetaData bmd3 = new BundleMetaData();
        bmd3.setFileName("bundle3.jar");
        String sum3 = "e89d21435b43d5d8607bbdc954e54774";
        bmd3.setExpectedCheckSum(sum3);
        app.addBundle(bmd3);

        return app;
    }
}
