package waqf.epub;

import static org.junit.Assert.*;

import org.junit.Test;
import waqf.test.TestConfig;


//FIXME: remove old epub library	

public class ePubGenTest {

	@Test
	public void test() {
		String template = "Besm Allah Elrahman Elraheem";
		String out = template.replaceAll("Allah", "Ellah");
		
		assertEquals("Besm Ellah Elrahman Elraheem", out);
		
	}
	
	
}
