package org.lcsim.detector.converter.compact;

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.lcsim.geometry.Detector;
import org.lcsim.geometry.GeometryReader;

/**
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class HPSEcalAPITest extends TestCase {
                  
    public void testHPSEcalAPI() throws Exception {
        GeometryReader geometryReader = new GeometryReader();
        InputStream in = getClass().getResourceAsStream("/org/lcsim/geometry/subdetector/HPSEcal3Test.xml");
        Detector detector = geometryReader.read(in);
        
        HPSEcalDetectorElement ecal = (HPSEcalDetectorElement)detector.getSubdetector("Ecal").getDetectorElement();
        
        HPSEcalAPI api = (HPSEcalAPI) ecal;
        
        assertEquals("The max X index is wrong.", 23, api.getXIndexMax());
        assertEquals("The min X index is wrong.", -23, api.getXIndexMin());
        assertEquals("The max Y index is wrong.", 5, api.getYIndexMax());
        assertEquals("The min Y index is wrong.", -5, api.getYIndexMin());
        
        for (Integer yIndex : api.getYIndices()) {
            if (yIndex == 0) {
                System.out.println("skipping yIndex = 0");
                continue;
            } 
            for (Integer xIndex : api.getXIndices()) {
                System.out.println("checking crystal " + xIndex + ", " + yIndex);
                if (xIndex == 0) {
                    System.out.println("skipping xIndex = 0");
                    continue;
                }                
                if((yIndex == 1 || yIndex == -1) && (xIndex <= -2 && xIndex >= -10)) {
                    System.out.println("crystal " + xIndex + ", " + yIndex + " should be in the gap");
                    assertTrue("Indices should be in gap: " + xIndex + ", " + yIndex, api.isInBeamGap(xIndex, yIndex));
                    // Crystal is in the beam gap.
                    continue;
                }
                EcalCrystal crystal = api.getCrystal(xIndex, yIndex);
                assertNotNull("Failed to find crystal at ix = " + xIndex + ", iy = " + yIndex, crystal);
            }
        }
        
        for (Integer yIndex : api.getYIndices()) {
            List<EcalCrystal> row = api.getRow(yIndex);
            System.out.println("found " + row.size() + " crystals in row " + yIndex);
            if (Math.abs(yIndex) != 1) {
                assertEquals("Wrong number of crystals in row.", 46, row.size());
            } else {
                assertEquals("Wrong number of crystals in row.", 37, row.size());
            }                        
        }
        
        for (Integer xIndex : api.getXIndices()) {            
            List<EcalCrystal> column = api.getColumn(xIndex);
            System.out.println("found " + column.size() + " crystals in column " + xIndex);
            if (xIndex > -2 || xIndex < -10) {
                assertEquals("Wrong number of crystals in column.", 10, column.size());
            } else {
                assertEquals("Wrong number of crystals in column.", 8, column.size());
            }                        
        }
    }                   
}
