package org.patladj.weatheraxway.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PatlaDJ on 21.10.2017 г..
 */
public class TestModel {
	private String koicho;
	public int kokoint;
	public List<String> nekyvList;

	public TestModel() {
		this.koicho="az Sym Pencho";
		this.kokoint=5;
		this.nekyvList=new ArrayList<String>();
		this.nekyvList.add("spisyk item 1");
		this.nekyvList.add("spisyk item 2");
	}
}
