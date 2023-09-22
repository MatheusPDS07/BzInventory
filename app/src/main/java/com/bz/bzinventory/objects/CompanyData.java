package com.bz.bzinventory.objects;

import com.google.firebase.database.Exclude;

/**This class is an object of an company*/
public class CompanyData {

    /**attributes*/
    @Exclude
    private String name;
    private String cnpj;
    private String corporateName;
    private String gerencia;
    private String email;

    /**empty constructor needed for runtime*/
    public CompanyData() {}

    /**Getters and setters of Company*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getGerencia() {
        return gerencia;
    }

    public void setGerencia(String gerencia) {
        this.gerencia = gerencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
