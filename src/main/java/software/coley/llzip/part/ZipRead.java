package software.coley.llzip.part;

/**
 * IO operations for children of {@link ZipPart}.
 *
 * @author Matt Coley
 */
public interface ZipRead {
	/**
	 * @param data
	 * 		Data to read from.
	 * @param offset
	 * 		Initial offset in data to start at.
	 */
	void read(byte[] data, int offset);

	// TODO: Write conventions, then rename to 'ZipReadWrite'
}