/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbf.controlo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import sgbf.modelo.ModAutor;
import sgbf.modelo.ModUtente;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilIconesDaJOPtionPane;

/**
 *
 * @author Look
 */
public class ConAutor extends ConCRUD {
    
    @Override
    public boolean registar(Object objecto_registar, String operacao) {
        ModAutor autorMod = (ModAutor)objecto_registar;
        try{
            if(this.jaExiste(autorMod, operacao)){
                throw new UtilControloExcessao(operacao,"Erro ao verificar dados do Autor",Alert.AlertType.ERROR);
            }else{
                super.query = "INSERT INTO tcc.Autor (primeiro_nome,segundo_nome,contacto,email)"
                            + " VALUES (?,?,?,?)";
                super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
                super.preparedStatement.setString(1, autorMod.getPrimeiro_nome());
                super.preparedStatement.setString(2, autorMod.getSegundo_nome());
                super.preparedStatement.setString(3, autorMod.getContacto());
                super.preparedStatement.setString(4, autorMod.getEmail());
                return !super.preparedStatement.execute();
            }
        }catch(SQLException erro){
            throw new UtilControloExcessao(operacao,"Erro ao "+operacao+" Autor !\nErro: "+erro.getMessage(),Alert.AlertType.ERROR);
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public boolean alterar(Object objecto_alterar, String operacao) {
        ModAutor autorMod = (ModAutor)objecto_alterar;
        try{
            if(this.jaExiste(autorMod, operacao)){
                throw new UtilControloExcessao(operacao,"Erro ao verificar dados do Autor",Alert.AlertType.ERROR);
            }else{
                super.query = "UPDATE tcc.Autor set primeiro_nome=?, segundo_nome=?, contacto=?, email=? where idAutor=?";
                super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
                super.preparedStatement.setString(1, autorMod.getPrimeiro_nome());
                super.preparedStatement.setString(2, autorMod.getSegundo_nome());
                super.preparedStatement.setString(3, autorMod.getContacto());
                super.preparedStatement.setString(4, autorMod.getEmail());
                super.preparedStatement.setInt(5, autorMod.getIdAutor());
                return !super.preparedStatement.execute();
            }
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Autor !\nErro: "+erro.getMessage(), operacao,UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public boolean remover(Object objecto_remover, String operacao) {
        ModAutor autorMod = (ModAutor)objecto_remover;
        try{
            if(this.temDadosRelacionados(autorMod, operacao)){
               throw new UtilControloExcessao("Esta operação não pode ser executada\nO autor seleccionado possui dados relacionados ", operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
            }else{
                super.query = "delete from tcc.Autor where idAutor=?";
                super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
                super.preparedStatement.setInt(1,autorMod.getIdAutor());
                return !super.preparedStatement.execute();
            }
        }catch(SQLException erro){
           throw new UtilControloExcessao("Erro ao "+operacao+" Autor !\nErro: "+erro.getMessage(), operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public List<Object> listarTodos(String operacao) {
        List<Object> todosRegistos = new ArrayList<>();
        try{
            super.query = "select * from tcc.Autor order by primeiro_nome, data_modificacao asc";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.setResultset = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistos.add(this.pegarRegistos(super.setResultset,operacao));
            }
            return todosRegistos;
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Autor(es) !\nErro: "+erro.getMessage(), operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public List<Object> pesquisar(Object objecto_pesquisar, String operacao) {
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        ModAutor autorMod = (ModAutor)objecto_pesquisar;
        try{
            super.query = "select * from tcc.Autor where idAutor=? or "
                        + "(primeiro_nome or segundo_nome) like '%"+autorMod.getPrimeiro_nome()+"%'";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setInt(1, autorMod.getIdAutor());
            super.setResultset  = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistosEncontrados.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistosEncontrados;
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Autor(es) !\nErro: "+erro.getMessage(), operacao,UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }
    
    private Object pegarRegistos(ResultSet setResult,String operacao) throws SQLException{
        ModAutor autorMod = new ModAutor();
        autorMod.setIdAutor(setResult.getInt("idAutor"), operacao);
        autorMod.setPrimeiro_nome(setResult.getString("primeiro_nome"), operacao);
        autorMod.setSegundo_nome(setResult.getString("segundo_nome"), operacao);
        autorMod.setNomeCompleto(autorMod.getPrimeiro_nome()+" "+autorMod.getSegundo_nome(), operacao);
        autorMod.setContacto(setResult.getString("contacto"), operacao);
        autorMod.setEmail(setResult.getString("email"), operacao);
        autorMod.getUtilControloDaData().setData_registo(setResultset.getTimestamp("data_registo"), operacao);
        autorMod.getUtilControloDaData().setData_modificacao(setResultset.getTimestamp("data_modificacao"), operacao);
        return autorMod;
    }
    
    private boolean temDadosRelacionados(ModAutor autorMod, String operacao) throws SQLException{
        super.query = "select *from acervosescritos where Autor_idAutor=?";
        super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(super.query);
        super.preparedStatement.setInt(1, autorMod.getIdAutor());
        return super.setResultset.next();
    }
    
    private boolean jaExiste(ModAutor autorIntroduzido, String operacao){
        for(Object todosRegistos:  this.listarTodos(operacao)){
            ModAutor autorRegistado = (ModAutor)todosRegistos;
            autorRegistado.equals(autorIntroduzido, operacao);
        }
        return false;
    }

}
