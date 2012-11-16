package de.mwalden.tools.mysql2java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class mySQL2JavaConverter {
	public static void main(String[] args) {
		String input = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"Statement.dat"));
			String zeile = null;
			
			while ((zeile = in.readLine()) != null) {
				input += zeile;
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(input);
		String[] statements = input.split("CREATE TABLE");
		
		for (String stmt :statements){
			StringBuilder code = new StringBuilder();
			code.append("package de.database.mysql.tables;\n");
			if (stmt.length() < 5) continue;
			String name = stmt.substring(0, stmt.indexOf("("));
			name = name.replace('`', ' ');
			name = name.trim();
			code.append("public class "+name+"{\n");
			File f = new File("c:\\temp\\"+name+".java");
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] variables = stmt.substring(stmt.indexOf("("), stmt.lastIndexOf("")).split(",");
			for (String var :variables){
				String temp = var.replace("  " , "");
				String[] v = temp.split(" ");
				if(v.length > 2){
					String type = v[1];
					if(type.startsWith("int")) type = "int" ;
					if(type.startsWith("varchar")||type.startsWith("char")||type.startsWith("longvarchar")) type = "String";
					if(type.startsWith("bigint")) type = "Long";
					if(type.startsWith("double")) type = "Double";
					String varName = v[0];
					varName = varName.replace("`", "");
					varName = varName.replace("(", "" );
					varName = varName.replace(")", "");
					code.append(type + " "+varName + ";\n");
				}
			}
			code.append("}");
			System.out.println(code.toString());
			
		    try {
		        FileWriter writer = new FileWriter(f ,true);
 		        writer.write(code.toString());
		        writer.flush();
		        writer.close();
		     } catch (IOException e) {
		       e.printStackTrace();
		     }
			
		}
		
		System.out.println();
	}
}
