package org.lcsim.geometry.subdetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.lcsim.detector.IDetectorElement;
import org.lcsim.detector.IGeometryInfo;
import org.lcsim.detector.converter.compact.EcalCrystal;
import org.lcsim.detector.converter.compact.HPSEcalDetectorElement;
import org.lcsim.detector.identifier.IIdentifierHelper;
import org.lcsim.geometry.Detector;
import org.lcsim.geometry.GeometryReader;
import org.lcsim.geometry.compact.converter.lcdd.Main;
import org.lcsim.util.test.TestUtil.TestOutputFile;

/**
 * <p>
 * Test conversion of detector type {@link org.lcsim.geometry.subdetector.HPSEcal3} 
 * to LCDD, Heprep, and in-memory Java objects.
 * <p>
 * Also do a basic check of crystal object neighboring.  
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class HPSEcal3Test extends TestCase {
        
    public void testLCDD() throws Exception {
        InputStream in = getClass().getResourceAsStream("/org/lcsim/geometry/subdetector/HPSEcal3Test.xml");
        new TestOutputFile(getClass().getSimpleName()).mkdir();
        File file = new TestOutputFile(getClass().getSimpleName()  + File.separator + "HPSEcal3Test.lcdd");
        OutputStream out = new FileOutputStream(file);
        new Main().convert("HPSEcal3Test", in, out);
    }
    
    public void testHeprep() throws Exception {
        InputStream in = getClass().getResourceAsStream("/org/lcsim/geometry/subdetector/HPSEcal3Test.xml");
        new TestOutputFile(getClass().getSimpleName()).mkdir();
        File file = new TestOutputFile(getClass().getSimpleName()  + File.separator + "HPSEcal3Test.heprep");
        OutputStream out = new FileOutputStream(file);
        new org.lcsim.geometry.compact.converter.heprep.Main().convert("HPSEcal3Test", in, out);
    }
    
    public void testDetector() throws Exception {
        GeometryReader geometryReader = new GeometryReader();
        InputStream in = getClass().getResourceAsStream("/org/lcsim/geometry/subdetector/HPSEcal3Test.xml");
        Detector detector = geometryReader.read(in);
        System.out.println("built detector " + detector.getName());
        
        IDetectorElement de = detector.getSubdetector("Ecal").getDetectorElement();
        System.out.println("ECAL has " + de.getChildren().size() + " crystals.");
        IIdentifierHelper helper = de.getIdentifierHelper();
        for (IDetectorElement crystal : de.getChildren()) {
            IGeometryInfo geometry = crystal.getGeometry();
            System.out.println(crystal.getName() + " - " + helper.unpack(crystal.getIdentifier()) + " @ " + geometry.getPosition());
        }        
    }
        
    public void testNeighbors() throws Exception {
        GeometryReader geometryReader = new GeometryReader();
        InputStream in = getClass().getResourceAsStream("/org/lcsim/geometry/subdetector/HPSEcal3Test.xml");
        Detector detector = geometryReader.read(in);
        
        HPSEcalDetectorElement ecal = (HPSEcalDetectorElement)detector.getSubdetector("Ecal").getDetectorElement();
        
        for (EcalCrystal crystal : ecal.getCrystals()) {
            System.out.println(crystal.getName() + " @ " + crystal.getX() + " " + crystal.getY() + " has neighbors ...");
            // Check this crystal object is valid.
            checkValid(crystal);
            for (EcalCrystal neighbor : ecal.getNeighbors(crystal)) {
                System.out.println("  " + neighbor.getX() + " " + neighbor.getY());
                // Check that each neighbor is valid.
                checkValid(neighbor);
            }
        }
    }      
             
    void checkValid(EcalCrystal crystal) {
        int ix = crystal.getX();
        int iy = crystal.getY();
        if (Math.abs(ix) > 23) throw new RuntimeException("ix is invalid: " + ix);
        if (Math.abs(iy) > 5) throw new RuntimeException("iy is invalid: " + iy);
        if (ix == 0) throw new RuntimeException("ix is zero");
        if (iy == 0) throw new RuntimeException("iy is zero");            
        if (Math.abs(iy) == 1) {
            if (ix <= -2 && ix >= -10) {
                throw new RuntimeException("crystal " + ix + " " + iy + " is in beam gap!");
            }
        }
    }
}
