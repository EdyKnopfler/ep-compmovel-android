package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class User{
	private String name;
	private String nusp;
	private Integer isProfessor; //1 Professor, 0 Student
  
	public void User(String name, String nusp, Integer isProfessor){
		this.name = name;
		this.nusp = nusp;
		this.isProfessor = isProfessor;
	}

	public String getNusp(){
		return this.nusp;
	}

	public String getName(){
		return this.name;
	}

	public Integer getIsProfessor(){
		return this.isProfessor;
	}

       //criar os sets, se necessario alterar cadastro	
}
