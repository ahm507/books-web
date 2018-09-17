package net.elazhar.scanner;


public class TextUtils {

    //function stripAccents(text) {
    //    return text.replace(new RegExp('[\u064B-\u065F]*', 'g'), '');
    //}


	static public String removeVowels(String text) {
		final int ARABIC_FATHATAN = 0x064B;
		final int ARABIC_DAMMATAN = 0x064C; 
		final int ARABIC_KASRATAN = 0x064D;
		final int ARABIC_FATHA = 0x064E;
		final int ARABIC_DAMMA = 0x064F;
		final int ARABIC_KASRA = 0x0650;
		final int ARABIC_SHADDA = 0x0651;
		final int ARABIC_SUKUN = 0x0652;
		StringBuffer text2 = new StringBuffer();
		for(int i=0 ; i < text.length(); i++) {
			switch(text.charAt(i)) {
				case ARABIC_FATHATAN:
				case ARABIC_DAMMATAN: 
				case ARABIC_KASRATAN:
				case ARABIC_FATHA:
				case ARABIC_DAMMA:
				case ARABIC_KASRA:
				case ARABIC_SHADDA:
				case ARABIC_SUKUN:
					break;
				default: //other chars
					text2.append(text.charAt(i));
					break;
			}
		}
		return text2.toString(); 
	}

	/**
	 * This is hardcoded shortcut to clean up markup tags typically #L0 to #L10
	 * It uses regular expression
	 * @param title The text to be cleaned
	 * @return
	 */
	static public String cleanupTitle(String title) {
		return title.replaceAll("#L\\d+\\s", "");//remove #L0, #L1, and so on.
	}

}
