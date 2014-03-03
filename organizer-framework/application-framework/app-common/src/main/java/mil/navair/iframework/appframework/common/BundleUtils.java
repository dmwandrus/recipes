/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dandrus
 */
public class BundleUtils
{
    private static final Logger LOGGER = Logger.getLogger(BundleUtils.class.getName());
    
    public static String getMd5Sum(String fileName)
    {
        InputStream is = null;
        byte[] digest = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            is = new FileInputStream(fileName);
            is = new DigestInputStream(is, md);

            digest = md.digest();
        } catch (FileNotFoundException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                is.close();
            } catch (IOException ex)
            {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return String.valueOf(digest);
    }
}
