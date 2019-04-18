package com.zhixin.test;

import java.io.File;
import java.util.ArrayList;

public class excelread {
	public static void main(String[] args) {
		File file=new File("E:\\新建 Microsoft Excel 工作表.xlsx");
		ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(file);
		for(int i = 0 ;i < result.size() ;i++){
			for(int j = 0;j<result.get(i).size(); j++){
				System.out.print(result.get(i).get(j).toString()+"\t");
			}
			System.out.println();
		}

	}

}
