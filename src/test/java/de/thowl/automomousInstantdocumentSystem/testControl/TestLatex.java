package de.thowl.automomousInstantdocumentSystem.testControl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import de.thowl.automomousInstantdocumentSystem.control.Latex;

public class TestLatex {

	/**
	 * This test checks if the tex file has been formated successfully
	 */
	@Test
	public void testConcat() {
		// Concatenate test Document
		Latex latex = new Latex();
		latex.gatherSnippets("test", 2, false);
		latex.concat("test");
		File testFile = new File("./temp/test.tex");
		File referenceFile = new File("./src/test/resources/latex/test/test.tex");
		try {
			String referenceFileContent = FileUtils.readFileToString(referenceFile, "utf-8");
			String testFileContent = FileUtils.readFileToString(testFile, "utf-8");
			assertEquals(referenceFileContent, testFileContent);
		} catch (IOException e) {
			fail("One or more files does not exist");
		}
	}

	/**
	 * This test checks if the pdf file has been compiled successfully
	 */
	@Test
	public void testCompile() {
		String type = "test";
		String destination = "temp/";
		Latex latex = new Latex();
		latex.gatherSnippets("test", 2, false);
		latex.concat(type);
		latex.compile(type, destination);
		if (!new File(destination + type + ".pdf").exists()) {
			fail("PDF not compiled");
		}
	}
}
