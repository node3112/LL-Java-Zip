package software.coley.llzip.part;

import software.coley.llzip.util.BufferData;
import software.coley.llzip.util.ByteData;
import software.coley.llzip.util.ByteDataUtil;

import java.util.Objects;


/**
 * ZIP EndOfCentralDirectory structure.
 *
 * @author Matt Coley
 */
public class EndOfCentralDirectory implements ZipPart, ZipRead {
	private transient long offset = -1L;
	// Zip spec elements
	private int diskNumber;
	private int centralDirectoryStartDisk;
	private int centralDirectoryStartOffset; // TODO: spec and wikipedia articles disagree about purpose?
	private int numEntries;
	private int centralDirectorySize;
	private int centralDirectoryOffset;
	private int zipCommentLength;
	private ByteData zipComment;
	private transient String zipCommentCache;

	@Override
	public void read(ByteData data, long offset) {
		this.offset = offset;
		diskNumber = ByteDataUtil.readWord(data, offset + 4);
		centralDirectoryStartDisk = ByteDataUtil.readWord(data, offset + 6);
		centralDirectoryStartOffset = ByteDataUtil.readWord(data, offset + 8);
		numEntries = ByteDataUtil.readWord(data, offset + 10);
		centralDirectorySize = ByteDataUtil.readQuad(data, offset + 12);
		centralDirectoryOffset = ByteDataUtil.readQuad(data, offset + 16);
		zipCommentLength = ByteDataUtil.readWord(data, offset + 20);
		zipComment = data.sliceOf(offset + 22, zipCommentLength);
	}

	@Override
	public int length() {
		return 22 + (int) zipComment.length();
	}

	@Override
	public PartType type() {
		return PartType.END_OF_CENTRAL_DIRECTORY;
	}

	@Override
	public long offset() {
		return offset;
	}

	/**
	 * @return Disk number for multi-file archives, or {@code 0xFFFF} for ZIP64.
	 */
	public int getDiskNumber() {
		return diskNumber;
	}

	/**
	 * @param diskNumber
	 * 		Disk number for multi-file archives, or {@code 0xFFFF} for ZIP64.
	 */
	public void setDiskNumber(int diskNumber) {
		this.diskNumber = diskNumber;
	}

	/**
	 * @return The first disk number where the central directory starts at, or {@code 0xFFFF} for ZIP64.
	 */
	public int getCentralDirectoryStartDisk() {
		return centralDirectoryStartDisk;
	}

	/**
	 * @param centralDirectoryStartDisk
	 * 		The first disk number where the central directory starts at, or {@code 0xFFFF} for ZIP64.
	 */
	public void setCentralDirectoryStartDisk(int centralDirectoryStartDisk) {
		this.centralDirectoryStartDisk = centralDirectoryStartDisk;
	}

	/**
	 * @return ?
	 */
	public int getCentralDirectoryStartOffset() {
		return centralDirectoryStartOffset;
	}

	/**
	 * @param centralDirectoryStartOffset
	 * 		?
	 */
	public void setCentralDirectoryStartOffset(int centralDirectoryStartOffset) {
		this.centralDirectoryStartOffset = centralDirectoryStartOffset;
	}

	/**
	 * @return Number of {@link CentralDirectoryFileHeader} that should exist in the archive.
	 */
	public int getNumEntries() {
		return numEntries;
	}

	/**
	 * @param numEntries
	 * 		Number of {@link CentralDirectoryFileHeader} that should exist in the archive.
	 */
	public void setNumEntries(int numEntries) {
		this.numEntries = numEntries;
	}

	/**
	 * @return Size of central directory in bytes or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public int getCentralDirectorySize() {
		return centralDirectorySize;
	}

	/**
	 * @param centralDirectorySize
	 * 		Size of central directory in bytes, or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public void setCentralDirectorySize(int centralDirectorySize) {
		this.centralDirectorySize = centralDirectorySize;
	}

	/**
	 * @return Offset of first {@link CentralDirectoryFileHeader} with respect to
	 * {@link #getCentralDirectoryStartDisk() starting disk number}, or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public int getCentralDirectoryOffset() {
		return centralDirectoryOffset;
	}

	/**
	 * @param centralDirectoryOffset
	 * 		Offset of first {@link CentralDirectoryFileHeader} with respect to
	 *        {@link #getCentralDirectoryStartDisk() starting disk number}, or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public void setCentralDirectoryOffset(int centralDirectoryOffset) {
		this.centralDirectoryOffset = centralDirectoryOffset;
	}

	/**
	 * @return {@link #getZipComment() Comment} length.
	 */
	public int getZipCommentLength() {
		return zipCommentLength;
	}

	/**
	 * @param zipCommentLength
	 *        {@link #getZipComment() Comment} length.
	 */
	public void setZipCommentLength(int zipCommentLength) {
		this.zipCommentLength = zipCommentLength;
	}

	/**
	 * @return Optional comment, or empty string.
	 */
	public ByteData getZipComment() {
		return zipComment;
	}

	/**
	 * @param zipComment
	 * 		Optional comment, or empty string.
	 */
	public void setZipComment(ByteData zipComment) {
		if (zipComment == null)
			zipComment = BufferData.wrap(new byte[0]);
		this.zipComment = zipComment;
	}
	
	public String getZipCommentAsString() {
		String zipCommentCache = this.zipCommentCache;
		if (zipCommentCache == null) {
			return this.zipCommentCache = ByteDataUtil.toString(zipComment);
		}
		return zipCommentCache;
	}

	@Override
	public String toString() {
		return "EndOfCentralDirectory{" +
				"offset=" + offset +
				", diskNumber=" + diskNumber +
				", centralDirectoryStartDisk=" + centralDirectoryStartDisk +
				", centralDirectoryStartOffset=" + centralDirectoryStartOffset +
				", numEntries=" + numEntries +
				", centralDirectorySize=" + centralDirectorySize +
				", centralDirectoryOffset=" + centralDirectoryOffset +
				", zipCommentLength=" + zipCommentLength +
				", zipComment='" + getZipCommentAsString() + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EndOfCentralDirectory that = (EndOfCentralDirectory) o;
		return offset == that.offset &&
				diskNumber == that.diskNumber &&
				centralDirectoryStartDisk == that.centralDirectoryStartDisk &&
				centralDirectoryStartOffset == that.centralDirectoryStartOffset &&
				numEntries == that.numEntries &&
				centralDirectorySize == that.centralDirectorySize &&
				centralDirectoryOffset == that.centralDirectoryOffset &&
				zipCommentLength == that.zipCommentLength &&
				zipComment.equals(that.zipComment);
	}

	@Override
	public int hashCode() {
		return Objects.hash(offset, diskNumber, centralDirectoryStartDisk, centralDirectoryStartOffset,
				numEntries, centralDirectorySize, centralDirectoryOffset, zipCommentLength, zipComment);
	}
}
