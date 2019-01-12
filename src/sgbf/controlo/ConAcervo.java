package sgbf.controlo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sgbf.modelo.ModAcervo;
import sgbf.modelo.ModAcervoFisico;
import sgbf.modelo.ModEstante;
import sgbf.util.UtilControloExcessao;
import sgbf.util.UtilIconesDaJOPtionPane;

/**
 *
 * @author Look
 */
public class ConAcervo extends ConCRUD {
    
    @Override
    public boolean registar(Object objecto_registar, String operacao) {
        ModAcervo acervoMod = (ModAcervoFisico)objecto_registar;
        try{
            super.query = "INSERT INTO tcc.acervos (titulo, subtittulo, tipo_acervo, formato, edicao, volume,"
                        + " numero_paginas, codigo_barra, isbn, idioma, ano_lancamento, sinopse, endereco_acervo,"
                        + " categoria_idcategoria, Editora_idEditora, Estoque_idEstoque)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setString(1, acervoMod.getTitulo());
            super.preparedStatement.setString(2, acervoMod.getSub_titulo());
            super.preparedStatement.setString(3, acervoMod.getTipo_acervo());
            super.preparedStatement.setString(4, acervoMod.getFormato());
            super.preparedStatement.setByte(5, acervoMod.getEdicao());
            super.preparedStatement.setByte(6, acervoMod.getVolume());
            super.preparedStatement.setInt(7, acervoMod.getNumero_paginas());
            //super.preparedStatement.setByte(8, acervoMod.get());
            //super.preparedStatement.setByte(9, acervoMod.getEdicao());
            super.preparedStatement.setString(10, acervoMod.getIdioma());
            super.preparedStatement.setInt(11, acervoMod.getAno_lancamento());
            //super.preparedStatement.setByte(12, acervoMod.getEdicao());
            //super.preparedStatement.setByte(13, acervoMod.getEdicao());
            super.preparedStatement.setInt(14, acervoMod.getCategoriaMod().getIdCategoria());
            //super.preparedStatement.setByte(15, acervoMod.get());
            super.preparedStatement.setInt(16, acervoMod.getEstoqueMod().getIdEstoque());
            return !super.preparedStatement.execute();
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Acervo !\nErro: "+erro.getMessage(), operacao,UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public boolean alterar(Object objecto_alterar, String operacao) {
        ModAcervo acervoMod = (ModAcervoFisico)objecto_alterar;
        try{
            super.query = "UPDATE tcc.acervos set titulo=?, subtittulo=?, tipo_acervo=?, formato=?, edicao=?, volume=?,"
                        + " numero_paginas=?, codigo_barra=?, isbn=?, idioma=?, ano_lancamento=?, sinopse=?, endereco_acervo=?,"
                        + " categoria_idcategoria=?, Editora_idEditora=?, Estoque_idEstoque=? where idAcervos=?";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setString(1, acervoMod.getTitulo());
            super.preparedStatement.setString(2, acervoMod.getSub_titulo());
            super.preparedStatement.setString(3, acervoMod.getTipo_acervo());
            super.preparedStatement.setString(4, acervoMod.getFormato());
            super.preparedStatement.setByte(5, acervoMod.getEdicao());
            super.preparedStatement.setByte(6, acervoMod.getVolume());
            super.preparedStatement.setInt(7, acervoMod.getNumero_paginas());
            //super.preparedStatement.setByte(8, acervoMod.get());
            //super.preparedStatement.setByte(9, acervoMod.getEdicao());
            super.preparedStatement.setString(10, acervoMod.getIdioma());
            super.preparedStatement.setInt(11, acervoMod.getAno_lancamento());
            //super.preparedStatement.setByte(12, acervoMod.getEdicao());
            //super.preparedStatement.setByte(13, acervoMod.getEdicao());
            super.preparedStatement.setInt(14, acervoMod.getCategoriaMod().getIdCategoria());
            //super.preparedStatement.setByte(15, acervoMod.get());
            super.preparedStatement.setInt(16, acervoMod.getEstoqueMod().getIdEstoque());
            super.preparedStatement.setInt(17, acervoMod.getIdAcervo());
            return !super.preparedStatement.execute();
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Acervo !\nErro: "+erro.getMessage(), operacao,UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    @Override
    public boolean remover(Object objecto_remover, String operacao) {
         ModAcervo acervoMod = (ModAcervoFisico)objecto_remover;
        try{
            if(this.temDadosRelacionados(acervoMod, operacao)){
               throw new UtilControloExcessao("Esta operacao não pode ser executada\nO Acervo seleccionado possui registo ! ", operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
            }else{
                super.query = "delete from tcc.acervos where idAcervos=?";
                super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
                super.preparedStatement.setInt(1,acervoMod.getIdAcervo());
                return !super.preparedStatement.execute();
            }
        }catch(SQLException erro){
           throw new UtilControloExcessao("Erro ao "+operacao+" Acervo !\nErro: "+erro.getMessage(), operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }
    
    private boolean temDadosRelacionados(ModAcervo acervoMod, String operacao) throws SQLException{
        super.query = "select *from itensSolicitados where Acervos_idAcervos=?";
        super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(super.query);
        super.preparedStatement.setInt(1, acervoMod.getIdAcervo());
        return super.setResultset.next();
    }

    @Override
    public List<Object> listarTodos(String operacao) {
        List<Object> todosRegistos = new ArrayList<>();
        /*try{
            super.query = "select * from tcc.acervos order by titulo, data_modificacao asc";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.setResultset = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistos.add(this.pegarRegistos(super.setResultset,operacao));
            }
            return todosRegistos;
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Acervo(s) !\nErro: "+erro.getMessage(), operacao, UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }*/
        return todosRegistos;
    }

    @Override
    public List<Object> pesquisar(Object objecto_pesquisar, String operacao) {
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        /*ModEstante estanteMod = (ModEstante)objecto_pesquisar;
        try{
            super.query = "select * from tcc.acervos where idAcervos=? or "
                        + "titulo like '%"+estanteMod.getDesignacao()+"%'";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setInt(1, estanteMod.getIdEstante());
            super.setResultset  = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistosEncontrados.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistosEncontrados;
        }catch(SQLException erro){
            throw new UtilControloExcessao("Erro ao "+operacao+" Acervo(s) !\nErro: "+erro.getMessage(), operacao,UtilIconesDaJOPtionPane.Erro.nomeDaImagem());
        }finally{
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }*/
        return todosRegistosEncontrados;
    }
    
    private Object pegarRegistos(ResultSet setResultset,String operacao) throws SQLException{
        ModEstante estanteMod = new ModEstante();
        /*estanteMod.setIdEstante(setResultset.getInt("idEstante"), operacao);
        estanteMod.setDesignacao(setResultset.getString("designacao"), operacao);
        estanteMod.setDescricao(setResultset.getString("descricacao"), operacao);
        estanteMod.setLinha(setResultset.getByte("linha"), operacao);
        estanteMod.setColuna(setResultset.getByte("coluna"), operacao);
        estanteMod.getAreaMod().setIdArea(setResultset.getInt("Area_idArea"), operacao);
        estanteMod.getUtilControloDaData().setData_registo(setResultset.getTimestamp("data_registo"), operacao);
        estanteMod.getUtilControloDaData().setData_modificacao(setResultset.getTimestamp("data_modificacao"), operacao);*/
        return estanteMod;
    }

    
}
