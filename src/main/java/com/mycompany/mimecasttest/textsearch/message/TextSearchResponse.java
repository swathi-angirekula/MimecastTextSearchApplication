package com.mycompany.mimecasttest.textsearch.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Swathi Angirekula
 *
 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TextSearchResponse implements Serializable, Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4109943550672693659L;
	private String server;
	private String filePath;
	private String searchText;
	private long count;
}
