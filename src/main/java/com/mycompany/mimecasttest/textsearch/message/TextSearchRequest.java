package com.mycompany.mimecasttest.textsearch.message;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Swathi Angirekula
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TextSearchRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5105150913348067829L;
	@NotNull
	private String server;
	@NotNull
	private String filePath;
	@NotNull
	private String searchTerm;

}
