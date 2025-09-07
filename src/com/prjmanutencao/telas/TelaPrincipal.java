/*
 * The MIT License
 *
 * Copyright 2025 fabio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prjmanutencao.telas;

import com.prjmanutencao.dal.ModuloConexao;
import static com.prjmanutencao.telas.TelaLogin.id;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Tela Principal que da acesso aos demais recursos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPrincipal extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String id_e_men = null;
    String id_e_tri = null;
    String id_e_sem = null;

    String id_h_men = null;
    String id_h_tri = null;
    String id_h_sem = null;

    String id_r_men = null;
    String id_r_tri = null;
    String id_r_sem = null;

    String id_c_men = null;
    String id_c_tri = null;
    String id_c_sem = null;

    String sta_e_men = null;
    String sta_e_trim = null;
    String sta_e_sem = null;

    String sta_h_men = null;
    String sta_h_trim = null;
    String sta_h_sem = null;

    String sta_r_men = null;
    String sta_r_trim = null;
    String sta_r_sem = null;

    String sta_c_men = null;
    String sta_c_trim = null;
    String sta_c_sem = null;

    String a = null;
    String n = null;
    String set = null;
    String tem = null;

    String ipDaMaquina = null;
    String nomeDaMaquina = null;
    String ip = null;

    boolean estado = true;
    String chamado;
    String setor;

    TelaChamados chamados;

    boolean estado1;
    boolean estado2;
    int segu = 0;

    /**
     * Construtor da classe TelaPrincipal. Responsável por inicializar os
     * componentes da interface, configurar o ícone da aplicação, estabelecer
     * conexão com o banco de dados e inicializar variáveis e métodos
     * necessários para a funcionalidade da tela.
     *
     * @author Fábio S. Oliveira
     * @version 1.1
     */
    public TelaPrincipal() {
        initComponents();
        setIcon();
        conexao = ModuloConexao.conector();
        estado = true;
        estado1 = true;
        quantideChamados();
        quantideChamadosAbertos();
        inicializacao();

        ipAcesso();
        a = "Aberta";
        n = "''";
        set = "Irregular";
        tem = "0";
        jLabel2.setBackground(new Color(242, 242, 242, 40));
        setor = "Todos";
        chamado = "todos";
    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção elétrica mensal. O resultado da
     * consulta é processado linha por linha e invoca o método "atu_ele_men"
     * para cada registro retornado.
     */
    private void select_eletr_men() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_form_ele_mensal, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_eletrica_mensal where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_e_men = (rs.getString(1));
                sta_e_men = (rs.getString(4));
                atu_ele_men();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * elétrica mensal no banco de dados. Verifica se o status atual corresponde
     * a uma condição específica e, em caso afirmativo, realiza a atualização.
     */
    private void atu_ele_men() {

        String status;
        status = sta_e_men;

        try {
            if (sta_e_men.equals(set)) {

                String sql = "update form_eletrica_mensal set  situacao_prev = ?  where id_form_ele_mensal = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_e_men);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção elétrica trimestral. O resultado
     * da consulta é processado e o método "atu_ele_tri" é invocado para cada
     * registro retornado.
     */
    private void select_eletr_tri() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_form_ele_trimestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_eletrica_trimestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_e_tri = (rs.getString(1));
                sta_e_trim = (rs.getString(4));
                atu_ele_tri();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * elétrica trimestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, em caso afirmativo, realiza a
     * atualização.
     */
    private void atu_ele_tri() {

        String status;
        status = sta_e_trim;
        try {
            if (sta_e_trim.equals(set)) {

                String sql = "update form_eletrica_trimestral set  situacao_prev = ?  where id_form_ele_trimestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_e_tri);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção elétrica semestral. O resultado da
     * consulta é processado e o método "atu_ele_sem" é invocado para cada
     * registro retornado.
     */
    private void select_eletr_sem() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_form_ele_semestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_eletrica_semestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_e_sem = (rs.getString(1));
                sta_e_sem = (rs.getString(4));
                atu_ele_sem();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * elétrica semestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_ele_sem() {

        String status;
        status = sta_e_sem;
        try {
            if (sta_e_sem.equals(set)) {

                String sql = "update form_eletrica_semestral set  situacao_prev = ?  where id_form_ele_semestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_e_sem);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção hidráulica mensal. O resultado da
     * consulta é processado e o método "atu_hid_men" é chamado para cada
     * registro retornado.
     */
    private void select_hid_men() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_hidraulica_mensal, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_hidraulica_mensal where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_h_men = (rs.getString(1));
                sta_h_men = (rs.getString(4));
                atu_hid_men();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void atu_hid_men() {

        String status;
        status = sta_h_men;

        try {
            if (sta_h_men.equals(set)) {

                String sql = "update form_hidraulica_mensal set  situacao_prev = ?  where id_hidraulica_mensal = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_h_men);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * hidráulica mensal no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void select_hid_tri() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_hidraulica_trimestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_hidraulica_trimestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_h_tri = (rs.getString(1));
                sta_h_trim = (rs.getString(4));
                atu_hid_tri();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * hidráulica trimestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_hid_tri() {

        String status;
        status = sta_h_trim;

        try {
            if (sta_h_trim.equals(set)) {

                String sql = "update form_hidraulica_trimestral set  situacao_prev = ?  where id_hidraulica_trimestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_h_tri);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção hidráulica semestral. O resultado
     * da consulta é processado e o método "atu_hid_sem" é invocado para cada
     * registro retornado.
     */
    private void select_hid_sem() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_hidraulica_semestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_hidraulica_semestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_h_sem = (rs.getString(1));
                sta_h_sem = (rs.getString(4));
                atu_hid_sem();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * hidráulica semestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_hid_sem() {

        String status;
        status = sta_h_sem;

        try {
            if (sta_h_sem.equals(set)) {

                String sql = "update form_hidraulica_semestral set  situacao_prev = ?  where id_hidraulica_semestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_h_sem);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção de refrigeração mensal. O
     * resultado da consulta é processado e o método "atu_ref_men" é chamado
     * para cada registro retornado.
     */
    private void select_ref_men() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_refrigeracao_mensal, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_refrigeracao_mensal where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_r_men = (rs.getString(1));
                sta_r_men = (rs.getString(4));
                atu_ref_men();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção de
     * refrigeração mensal no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_ref_men() {

        String status;
        status = sta_r_men;

        try {
            if (sta_r_men.equals(set)) {

                String sql = "update form_refrigeracao_mensal set  situacao_prev = ?  where id_refrigeracao_mensal = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_r_men);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção de refrigeração trimestral. O
     * resultado da consulta é processado, e o método "atu_ref_tri" é chamado
     * para cada registro retornado.
     */
    private void select_ref_tri() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_refrigeracao_trimestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_refrigeracao_trimestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_r_tri = (rs.getString(1));
                sta_r_trim = (rs.getString(4));
                atu_ref_tri();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção de
     * refrigeração trimestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_ref_tri() {

        String status;
        status = sta_r_trim;

        try {
            if (sta_r_trim.equals(set)) {

                String sql = "update form_refrigeracao_trimestral set  situacao_prev = ?  where id_refrigeracao_trimestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_r_tri);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção de refrigeração semestral. O
     * resultado da consulta é processado, e o método "atu_ref_sem" é chamado
     * para cada registro retornado.
     */
    private void select_ref_sem() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_refrigeracao_semestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_refrigeracao_semestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_r_sem = (rs.getString(1));
                sta_r_sem = (rs.getString(4));
                atu_ref_sem();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção de
     * refrigeração semestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_ref_sem() {

        String status;
        status = sta_r_sem;

        try {
            if (sta_r_sem.equals(set)) {

                String sql = "update form_refrigeracao_semestral set  situacao_prev = ?  where id_refrigeracao_semestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_r_sem);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção civil mensal. O resultado da
     * consulta é processado, e o método "atu_civ_men" é chamado para cada
     * registro retornado.
     */
    private void select_civ_men() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_civil_mensal, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_civil_mensal where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_c_men = (rs.getString(1));
                sta_c_men = (rs.getString(4));
                atu_civ_men();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * civil mensal no banco de dados. Verifica se o status atual corresponde a
     * uma condição específica e, caso positivo, realiza a atualização.
     */
    private void atu_civ_men() {

        String status;
        status = sta_c_men;

        try {
            if (sta_c_men.equals(set)) {

                String sql = "update form_civil_mensal set  situacao_prev = ?  where id_civil_mensal = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_c_men);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção civil trimestral. O resultado da
     * consulta é processado, e o método "atu_civ_tri" é chamado para cada
     * registro retornado.
     */
    private void select_civ_tri() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_civil_trimestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_civil_trimestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_c_tri = (rs.getString(1));
                sta_c_trim = (rs.getString(4));
                atu_civ_tri();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * civil trimestral no banco de dados. Verifica se o status atual
     * corresponde a uma condição específica e, caso positivo, realiza a
     * atualização.
     */
    private void atu_civ_tri() {

        String status;
        status = sta_c_trim;

        try {
            if (sta_c_trim.equals(set)) {

                String sql = "update form_civil_trimestral set  situacao_prev = ?  where id_civil_trimestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_c_tri);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por executar uma consulta no banco de dados para obter
     * informações de formulários de inspeção civil semestral. O resultado da
     * consulta é processado, e o método "atu_civ_sem" é chamado para cada
     * registro retornado.
     */
    private void select_civ_sem() {
        String sit = a;
        String nul = n;
        String z = tem;

        String sql = "select id_civil_semestral, situacao_prev, DATE_FORMAT(data_prev,'%d / %m / %Y'), if(month(data_prev) - month(CURDATE()) <(?) ,'Irregular','') as tipo from form_civil_semestral where situacao_prev is null or situacao_prev = ? or situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, z);
            pst.setString(2, nul);
            pst.setString(3, sit);
            rs = pst.executeQuery();
            while (rs.next()) {
                id_c_sem = (rs.getString(1));
                sta_c_sem = (rs.getString(4));
                atu_civ_sem();
            }
//                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar o status de um formulário de inspeção
     * civil semestral no banco de dados. Verifica se o status atual corresponde
     * a uma condição específica e, caso positivo, realiza a atualização.
     */
    private void atu_civ_sem() {

        String status;
        status = sta_c_sem;

        try {
            if (sta_c_sem.equals(set)) {

                String sql = "update form_civil_semestral set  situacao_prev = ?  where id_civil_semestral = ?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id_c_sem);
                int alterar = pst.executeUpdate();
                while (alterar > 1) {
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método responsável por inicializar um thread que executa consultas
     * sequenciais no banco de dados e realiza atualizações de status em
     * diferentes formulários. Também gerencia o fechamento da conexão com o
     * banco de dados e executa métodos adicionais.
     */
    private void inicializacao() {

        Thread cron = new Thread() {
            @Override
            public void run() {
                try {
                    select_eletr_men();
                    select_hid_men();
                    select_civ_men();
                    select_ref_men();
                    sleep(1000);
                    select_eletr_tri();
                    select_hid_tri();
                    select_civ_tri();
                    select_ref_tri();
                    sleep(1000);
                    select_eletr_sem();
                    select_hid_sem();
                    select_civ_sem();
                    select_ref_sem();
                    try {
                        conexao.close();
                        intermediario();
                    } catch (SQLException ex) {
                        Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }

        };

        cron.start();
    }

    /**
     * Método responsável por executar um loop contínuo em um thread que
     * verifica e atualiza informações periodicamente, baseado em um intervalo
     * definido. Durante a execução, ele gerencia o estado, realiza consultas e
     * atualizações no banco de dados e finaliza adequadamente a conexão.
     */
    private void intermediario() {
        atualizarTab();
        Thread cron = new Thread() {
            @Override
            public void run() {
                for (;;) {
                    if (estado == true) {
                        try {
                            sleep(43200000);
                            estado1 = false;
                            conexao = ModuloConexao.conector();
                            select_eletr_men();
                            select_hid_men();
                            select_civ_men();
                            select_ref_men();
                            select_eletr_tri();
                            select_hid_tri();
                            select_civ_tri();
                            select_ref_tri();
                            select_eletr_sem();
                            select_hid_sem();
                            select_civ_sem();
                            select_ref_sem();
                            try {
                                conexao.close();

                            } catch (SQLException ex) {
                                Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            estado1 = true;
                            atualizarTab();
                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }

                    } else {
                        break;
                    }

                }

            }

        };

        cron.start();
    }

    /**
     * Método responsável por encerrar os processos em execução relacionados às
     * consultas e atualizações. Define variáveis de estado como falso, realiza
     * consultas necessárias e fecha a conexão com o banco de dados. As
     * operações são realizadas de forma assíncrona em um thread separado.
     */
    private void encerrar() {

        Thread cron = new Thread() {
            @Override
            public void run() {
                try {
                    estado = false;
                    estado1 = false;
                    sleep(1000);
                    conexao = ModuloConexao.conector();
                    select_eletr_men();
                    select_hid_men();
                    select_civ_men();
                    select_ref_men();
                    sleep(1000);
                    select_eletr_tri();
                    select_hid_tri();
                    select_civ_tri();
                    select_ref_tri();
                    sleep(1000);
                    select_eletr_sem();
                    select_hid_sem();
                    select_civ_sem();
                    select_ref_sem();
                    try {
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }

        };

        cron.start();
    }

    /**
     * Método responsável por consultar o banco de dados para obter o maior
     * valor do campo "id_ace" da tabela "acesso". Esse valor é armazenado na
     * variável "ip", representando o último registro de acesso.
     */
    private void ipAcesso() {
        String sql = "select max(id_ace) from acesso";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                ip = (rs.getString(1));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por atualizar a tabela "acesso" no banco de dados,
     * preenchendo o campo "data_said" com a data e hora atuais para o registro
     * correspondente ao ID especificado.
     */
    private void acesso() {

        conexao = ModuloConexao.conector();
        String data;

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        data = sdf3.format(timestamp);

        String sql = "update acesso set data_said = ? where id_ace = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, data);
            pst.setString(2, ip);
            int Adicionar = pst.executeUpdate();
            if (Adicionar > 0) {
            }
        } catch (SQLException ex) {
            Logger.getLogger(TelaLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método responsável por atualizar o status de um usuário no banco de
     * dados, alterando o campo "status_usuario" para "off-line" com base no ID
     * fornecido.
     */
    private void statusUsario() {
        conexao = ModuloConexao.conector();
        String str = "off-line";
        String sql = "update usuarios set status_usuario = ? where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, str);
            pst.setString(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por consultar a quantidade de chamados abertos (OS) de
     * um usuário no banco de dados. O resultado é exibido no botão de
     * notificação (btnNotificar).
     */
    private void quantideChamados() {

        conexao = ModuloConexao.conector();
        String status = "Aberta";

        String sql = "select count(*) from os  where iduser = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                if ("0".equals(rs.getString(1))) {
                    btnNotificar.setText("");
                } else {
                    btnNotificar.setText(rs.getString(1));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por consultar a quantidade de chamados abertos (OS) de
     * um usuário específico no banco de dados. O resultado é exibido no botão
     * de notificação (btnNotificar1).
     */
    private void quantideChamadosAbertos() {
        conexao = ModuloConexao.conector();
        String status = "Aberta";

        String sql = "select count(*) from os  where iduser1 = ? and situacao_os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                if ("0".equals(rs.getString(1))) {
                    btnNotificar1.setText("");
                } else {
                    btnNotificar1.setText(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por consultar a quantidade de chamados abertos (OS) de
     * um usuário específico no banco de dados. O resultado é exibido no botão
     * de notificação (btnNotificar1).
     */
    private void atualizarTab() {

        Thread cron1 = new Thread() {
            @Override
            public void run() {

                for (;;) {
                    if (estado1 == true) {
                        try {
                            sleep(5000);
                            quantideChamados();
                            quantideChamadosAbertos();
                            try {
                                conexao.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    } else {
                        break;
                    }
                }

            }
        };
        cron1.start();
    }

    /**
     * Método responsável por consultar a quantidade de chamados abertos (OS) de
     * um usuário específico no banco de dados. O resultado é exibido no botão
     * de notificação (btnNotificar1).
     */
    private void relatorioOs() {

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            estado1 = false;
            conexao = ModuloConexao.conector();

            try {
                InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/relatorioOs.jasper");
                URL diretorioBase = getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png");

                // Adiciona os parâmetros
//                Map<String, Object> parameters = new HashMap<>();

                JasperPrint print;
                JasperViewer viewer;
                Image icone;

                // Define o diretório base para os recursos do relatório
//                parameters.put("REPORT_DIR2", diretorioBase); // Caminho relativo configurado

                print = JasperFillManager.fillReport(caminhoRelativo, null, conexao);
                viewer = new JasperViewer(print, false);
                // Define o título da janela
                viewer.setTitle("Relatório");

                icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));

                viewer.setIconImage(icone);
                viewer.setVisible(true);
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (NumberFormatException | JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        desktop = new javax.swing.JDesktopPane();
        lblUsuario = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        btnNotificar = new com.prjmanutencao.telas.BadgeButton();
        btnNotificar1 = new com.prjmanutencao.telas.BadgeButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        men = new javax.swing.JMenuBar();
        menCad = new javax.swing.JMenu();
        menCadUsu = new javax.swing.JMenuItem();
        menCadSet = new javax.swing.JMenuItem();
        menOs = new javax.swing.JMenu();
        menOsAbr = new javax.swing.JMenuItem();
        menOsAbeFec = new javax.swing.JMenuItem();
        menOsChamados = new javax.swing.JMenuItem();
        menPreSemEnc = new javax.swing.JMenu();
        menPreAbr = new javax.swing.JMenuItem();
        menPreMen = new javax.swing.JMenuItem();
        menPreSemEnce = new javax.swing.JMenuItem();
        menEst = new javax.swing.JMenu();
        menEstCadAlt = new javax.swing.JMenuItem();
        menEstRetProd = new javax.swing.JMenuItem();
        menRel = new javax.swing.JMenu();
        menRelOs = new javax.swing.JMenuItem();
        menRelPre = new javax.swing.JMenuItem();
        menAju = new javax.swing.JMenu();
        memAjuSob = new javax.swing.JMenuItem();
        menOpc = new javax.swing.JMenu();
        menOpcSai = new javax.swing.JMenuItem();
        menOpcAce = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sistema - Tela Inicial");
        setExtendedState(6);
        setMinimumSize(new java.awt.Dimension(600, 400));
        setSize(new java.awt.Dimension(0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        desktop.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153)));
        desktop.setMaximumSize(new java.awt.Dimension(1000, 720));
        desktop.setMinimumSize(new java.awt.Dimension(1000, 720));
        desktop.setOpaque(false);
        desktop.setPreferredSize(new java.awt.Dimension(1010, 720));

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1006, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(desktop);
        desktop.setBounds(0, 0, 1010, 672);

        lblUsuario.setFont(new java.awt.Font("Arial Black", 1, 15)); // NOI18N
        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsuario.setText("Usuário");
        lblUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblUsuario);
        lblUsuario.setBounds(1012, 596, 272, 22);

        lblData.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblData.setText("Data");
        getContentPane().add(lblData);
        lblData.setBounds(1012, 622, 272, 20);

        lblHora.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        lblHora.setForeground(new java.awt.Color(0, 0, 153));
        lblHora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora.setText("-- : -- : --");
        getContentPane().add(lblHora);
        lblHora.setBounds(1012, 644, 272, 20);

        btnNotificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/sino1.png"))); // NOI18N
        btnNotificar.setToolTipText("Chamados Para Atender");
        btnNotificar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNotificar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNotificar.setRolloverEnabled(false);
        btnNotificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnNotificar);
        btnNotificar.setBounds(1286, 590, 75, 40);

        btnNotificar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/sino1.png"))); // NOI18N
        btnNotificar1.setToolTipText("Atendimento Solicitado");
        btnNotificar1.setBadgeColor(new java.awt.Color(34, 159, 94));
        btnNotificar1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNotificar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNotificar1.setRolloverEnabled(false);
        btnNotificar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificar1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnNotificar1);
        btnNotificar1.setBounds(1286, 630, 75, 40);

        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(1010, 588, 360, 84);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/telaprincipal.jpg"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1280, 720));
        jLabel1.setMinimumSize(new java.awt.Dimension(1280, 730));
        jLabel1.setPreferredSize(new java.awt.Dimension(1280, 720));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -23, 1390, 720);

        men.setBorder(new javax.swing.border.MatteBorder(null));
        men.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        men.setPreferredSize(new java.awt.Dimension(418, 25));
        men.setRequestFocusEnabled(false);

        menCad.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menCad.setText("Cadastro");
        menCad.setEnabled(false);
        menCad.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        menCad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menCad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        menCadUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        menCadUsu.setText("Usuário");
        menCadUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadUsuActionPerformed(evt);
            }
        });
        menCad.add(menCadUsu);

        menCadSet.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menCadSet.setText("Setor");
        menCadSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadSetActionPerformed(evt);
            }
        });
        menCad.add(menCadSet);

        men.add(menCad);

        menOs.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menOs.setText("Ordem de Serviço");
        menOs.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        menOs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menOs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        menOsAbr.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menOsAbr.setText("Abrir O.S");
        menOsAbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOsAbrActionPerformed(evt);
            }
        });
        menOs.add(menOsAbr);

        menOsAbeFec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menOsAbeFec.setText("O.S Aberta/Encerrada");
        menOsAbeFec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOsAbeFecActionPerformed(evt);
            }
        });
        menOs.add(menOsAbeFec);

        menOsChamados.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        menOsChamados.setText("Chamados");
        menOsChamados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOsChamadosActionPerformed(evt);
            }
        });
        menOs.add(menOsChamados);

        men.add(menOs);

        menPreSemEnc.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menPreSemEnc.setText("Preventiva");
        menPreSemEnc.setEnabled(false);
        menPreSemEnc.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        menPreAbr.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        menPreAbr.setText("Abrir Preventiva");
        menPreAbr.setEnabled(false);
        menPreAbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menPreAbrActionPerformed(evt);
            }
        });
        menPreSemEnc.add(menPreAbr);

        menPreMen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        menPreMen.setText("Iniciar/Em Aberto");
        menPreMen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menPreMenActionPerformed(evt);
            }
        });
        menPreSemEnc.add(menPreMen);

        menPreSemEnce.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        menPreSemEnce.setText("Encerrada/Irregular");
        menPreSemEnce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menPreSemEnceActionPerformed(evt);
            }
        });
        menPreSemEnc.add(menPreSemEnce);

        men.add(menPreSemEnc);

        menEst.setText("Estoque");
        menEst.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        menEst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        menEstCadAlt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menEstCadAlt.setText("Cadastro /Alteração");
        menEstCadAlt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menEstCadAltActionPerformed(evt);
            }
        });
        menEst.add(menEstCadAlt);

        menEstRetProd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menEstRetProd.setText("Retirada ");
        menEstRetProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menEstRetProdActionPerformed(evt);
            }
        });
        menEst.add(menEstRetProd);

        men.add(menEst);

        menRel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menRel.setText("Relatório");
        menRel.setEnabled(false);
        menRel.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        menRelOs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menRelOs.setText("O.S");
        menRelOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelOsActionPerformed(evt);
            }
        });
        menRel.add(menRelOs);

        menRelPre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menRelPre.setText("Preventiva");
        menRelPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelPreActionPerformed(evt);
            }
        });
        menRel.add(menRelPre);

        men.add(menRel);

        menAju.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menAju.setText("Ajuda");
        menAju.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        memAjuSob.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        memAjuSob.setText("Sobre");
        memAjuSob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memAjuSobActionPerformed(evt);
            }
        });
        menAju.add(memAjuSob);

        men.add(menAju);

        menOpc.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));
        menOpc.setText("Opções");
        menOpc.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        menOpc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menOpc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        menOpcSai.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menOpcSai.setText("Sair");
        menOpcSai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOpcSaiActionPerformed(evt);
            }
        });
        menOpc.add(menOpcSai);

        menOpcAce.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menOpcAce.setText("Acessos");
        menOpcAce.setEnabled(false);
        menOpcAce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOpcAceActionPerformed(evt);
            }
        });
        menOpc.add(menOpcAce);

        men.add(menOpc);

        setJMenuBar(men);

        setSize(new java.awt.Dimension(1386, 737));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método acionado ao selecionar o menu para cadastro de usuários. Ele
     * verifica se a tela de "chamados" está aberta e, em caso positivo, a fecha
     * antes de abrir a tela "TelaUsuarios". A nova tela é adicionada ao desktop
     * e maximizada.
     *
     * @param evt Evento acionado pelo clique no menu.
     */
    private void menCadUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadUsuActionPerformed
        // As linhas a baixo chamam a tela usuarios
        if (chamados != null) {
            chamados.fechar();
            TelaUsuarios usuarios = new TelaUsuarios();
            usuarios.setVisible(true);
            desktop.add(usuarios);
            try {
                usuarios.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaUsuarios usuarios = new TelaUsuarios();
            usuarios.setVisible(true);
            desktop.add(usuarios);
            try {
                usuarios.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menCadUsuActionPerformed

    /**
     * Método acionado ao selecionar o menu para cadastro de setores. Verifica
     * se a tela "chamados" está aberta e, caso esteja, a fecha antes de abrir a
     * tela "TelaSetor". A nova tela é adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique no menu.
     */
    private void menCadSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadSetActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaSetor se = new TelaSetor();
            se.setVisible(true);
            desktop.add(se);
            try {
                se.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaSetor se = new TelaSetor();
            se.setVisible(true);
            desktop.add(se);
            try {
                se.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menCadSetActionPerformed

    /**
     * Método acionado ao selecionar o menu para abrir a tela de ordens de
     * serviço (OS). Verifica se a tela "chamados" está aberta e, caso esteja, a
     * fecha antes de abrir a tela "TelaOs1". A nova tela é adicionada ao
     * desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique no menu.
     */
    private void menOsAbrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOsAbrActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaOs1 os = new TelaOs1();
            os.setVisible(true);
            desktop.add(os);
            try {
                os.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaOs1 os = new TelaOs1();
            os.setVisible(true);
            desktop.add(os);
            try {
                os.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menOsAbrActionPerformed

    private void memAjuSobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memAjuSobActionPerformed
        // Chamando tela sobre
        TelaSobre sobre = new TelaSobre();
        sobre.setVisible(true);
    }//GEN-LAST:event_memAjuSobActionPerformed

    /**
     * Método acionado ao selecionar a opção de sair do programa no menu.
     * Solicita uma confirmação ao usuário antes de encerrar o programa,
     * executando os métodos necessários para atualizar o status do usuário,
     * registrar o acesso e encerrar processos ativos.
     *
     * @param evt Evento acionado pelo clique na opção de saída.
     */
    private void menOpcSaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOpcSaiActionPerformed

        if (conexao == null) {

        } else {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Realmente Fechar este Programa?\n Você Podera estar Perdendo Dados!", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                statusUsario();
                acesso();
                encerrar();
                try {
                    sleep(1000);
                    System.exit(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }//GEN-LAST:event_menOpcSaiActionPerformed

    /**
     * Método acionado ao selecionar a opção para abrir a tela de pesquisa de
     * ordens de serviço (OS). Verifica se a tela "chamados" está aberta e, caso
     * esteja, a fecha antes de abrir a tela "TelaPesquisaros". A nova tela é
     * adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menOsAbeFecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOsAbeFecActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaPesquisaros pesquisar = new TelaPesquisaros();
            pesquisar.setVisible(true);
            desktop.add(pesquisar);
            try {
                pesquisar.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaPesquisaros pesquisar = new TelaPesquisaros();
            pesquisar.setVisible(true);
            desktop.add(pesquisar);
            try {
                pesquisar.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_menOsAbeFecActionPerformed

    /**
     * Método acionado ao selecionar a opção para abrir a tela de distribuição
     * de preventivas. Verifica se a tela "chamados" está aberta e, caso esteja,
     * a fecha antes de abrir a tela "TelaDisPreventiva". A nova tela é
     * adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menPreAbrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menPreAbrActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaDisPreventiva dispreventivas = new TelaDisPreventiva();
            dispreventivas.setVisible(true);
            desktop.add(dispreventivas);
            try {
                dispreventivas.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaDisPreventiva dispreventivas = new TelaDisPreventiva();
            dispreventivas.setVisible(true);
            desktop.add(dispreventivas);
            try {
                dispreventivas.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menPreAbrActionPerformed

    /**
     * Método acionado ao selecionar o menu para abrir a lista de preventivas.
     * Verifica se a tela "chamados" está aberta e, caso esteja, a fecha antes
     * de abrir a tela "TelaPrevListas". A nova tela é adicionada ao desktop e
     * maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menPreMenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menPreMenActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaPrevListas abe = new TelaPrevListas();
            abe.setVisible(true);
            desktop.add(abe);
            try {
                abe.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } else {
            TelaPrevListas abe = new TelaPrevListas();
            abe.setVisible(true);
            desktop.add(abe);
            try {
                abe.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menPreMenActionPerformed

    /**
     * Método acionado quando o evento de fechamento da janela principal é
     * detectado. Solicita uma confirmação ao usuário antes de encerrar o
     * programa, executando as ações necessárias para atualizar o status do
     * usuário, registrar o acesso e encerrar processos ativos.
     *
     * @param evt Evento acionado ao tentar fechar a janela principal.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (conexao == null) {
            System.exit(0);
        } else {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Realmente Fechar este Programa?\n Você Podera estar Perdendo Dados!", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                statusUsario();
                acesso();
                encerrar();
                try {
                    sleep(1000);
                    System.exit(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * Método acionado ao selecionar a opção para abrir a lista de preventivas
     * encerradas. Verifica se a tela "chamados" está aberta e, caso esteja, a
     * fecha antes de abrir a tela "TelaPrevEncerradas". A nova tela é
     * adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menPreSemEnceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menPreSemEnceActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaPrevEncerradas ence = new TelaPrevEncerradas();
            ence.setVisible(true);
            desktop.add(ence);
            try {
                ence.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaPrevEncerradas ence = new TelaPrevEncerradas();
            ence.setVisible(true);
            desktop.add(ence);
            try {
                ence.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menPreSemEnceActionPerformed

    /**
     * Método acionado automaticamente quando a janela principal é aberta.
     * Configura a exibição da data atual no formato completo e inicializa um
     * temporizador que atualiza a hora em intervalos de 1 segundo.
     *
     * @param evt Evento acionado quando a janela principal é aberta.
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
        lblData.setText(formatador.format(data));

        Timer timer = new Timer(1000, new hora());
        timer.start();

    }//GEN-LAST:event_formWindowOpened

    /**
     * Método acionado ao selecionar a opção para abrir a tela de controle de
     * acesso. Verifica se a tela "chamados" está aberta e, caso esteja, a fecha
     * antes de abrir a tela "TelaAcesso". A nova tela é adicionada ao desktop e
     * maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menOpcAceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOpcAceActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaAcesso acesso = new TelaAcesso();
            acesso.setVisible(true);
            desktop.add(acesso);
            try {
                acesso.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaAcesso acesso = new TelaAcesso();
            acesso.setVisible(true);
            desktop.add(acesso);
            try {
                acesso.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menOpcAceActionPerformed

    /**
     * Método acionado ao selecionar a opção do menu para acessar a tela de
     * chamados. Verifica se a instância da tela "chamados" está inicializada.
     * Se não estiver, cria uma nova instância. Caso esteja, a tela atual é
     * fechada antes de criar e exibir uma nova. A nova tela é adicionada ao
     * desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menOsChamadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOsChamadosActionPerformed

        if (chamados == null) {
            chamados = new TelaChamados();
            chamados.setVisible(true);
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            chamados.fechar();
            chamados = new TelaChamados();
            chamados.setVisible(true);
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menOsChamadosActionPerformed

    /**
     * Método acionado ao clicar no botão de notificação. Abre a tela de
     * chamados, garantindo que ela esteja visível e pronta para exibir as
     * informações associadas. Se a tela "chamados" já existir, ela é fechada
     * antes de ser recriada e exibida.
     *
     * @param evt Evento acionado pelo clique no botão de notificação.
     */
    private void btnNotificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificarActionPerformed

        if (chamados == null) {
            chamados = new TelaChamados();
            chamados.setVisible(true);
            TelaChamados.rbtChamados.setSelected(true);
            chamados.chamados();
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            chamados.fechar();
            chamados = new TelaChamados();
            chamados.setVisible(true);
            TelaChamados.rbtChamados.setSelected(true);
            chamados.chamados();
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnNotificarActionPerformed

    /**
     * Método acionado ao clicar no botão de notificação 1. Abre a tela de
     * chamados solicitados, garantindo que ela esteja visível e pronta para
     * exibir informações relacionadas. Se a tela "chamados" já existir, ela é
     * fechada antes de ser recriada e exibida.
     *
     * @param evt Evento acionado pelo clique no botão de notificação 1.
     */
    private void btnNotificar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificar1ActionPerformed

        if (chamados == null) {
            chamados = new TelaChamados();
            chamados.setVisible(true);
            TelaChamados.rbtChaSolicitados.setSelected(true);
            chamados.chamadosAberto();
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            chamados.fechar();
            chamados = new TelaChamados();
            chamados.setVisible(true);
            TelaChamados.rbtChaSolicitados.setSelected(true);
            chamados.chamadosAberto();
            desktop.add(chamados);
            try {
                chamados.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnNotificar1ActionPerformed

    /**
     * Método acionado ao selecionar a opção do menu para gerar relatórios de
     * Ordens de Serviço (OS). Chama o método `relatorioOs()` para executar as
     * operações necessárias.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menRelOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelOsActionPerformed

        relatorioOs();
    }//GEN-LAST:event_menRelOsActionPerformed

    /**
     * Método acionado ao selecionar a opção do menu para gerar relatórios de
     * preventivas. Cria uma nova instância da tela "TelaRelPreventivas1",
     * tornando-a visível e adicionando-a ao desktop.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menRelPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelPreActionPerformed

        TelaRelPreventivas1 relatorios = new TelaRelPreventivas1();
        relatorios.setVisible(true);
        desktop.add(relatorios);
    }//GEN-LAST:event_menRelPreActionPerformed

    /**
     * Método acionado ao selecionar a opção de menu para cadastro ou alteração
     * de produtos. Verifica se a tela "chamados" está aberta e, caso esteja, a
     * fecha antes de abrir a tela "TelaCadastrarProduto". A nova tela é
     * adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menEstCadAltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menEstCadAltActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaCadastrarProduto produto = new TelaCadastrarProduto();
            produto.setVisible(true);
            desktop.add(produto);
            try {
                produto.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaCadastrarProduto produto = new TelaCadastrarProduto();
            produto.setVisible(true);
            desktop.add(produto);
            try {
                produto.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menEstCadAltActionPerformed

    /**
     * Método acionado ao selecionar a opção do menu para registrar a retirada
     * de produtos. Verifica se a tela "chamados" está aberta. Caso esteja,
     * fecha essa tela antes de abrir a nova tela "TelaSaidaProdutos". A nova
     * tela é adicionada ao desktop e maximizada.
     *
     * @param evt Evento acionado pelo clique na opção do menu.
     */
    private void menEstRetProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menEstRetProdActionPerformed

        if (chamados != null) {
            chamados.fechar();
            TelaSaidaProdutos retirada = new TelaSaidaProdutos();
            retirada.setVisible(true);
            desktop.add(retirada);
            try {
                retirada.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            TelaSaidaProdutos retirada = new TelaSaidaProdutos();
            retirada.setVisible(true);
            desktop.add(retirada);
            try {
                retirada.setMaximum(true);
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_menEstRetProdActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.prjmanutencao.telas.BadgeButton btnNotificar;
    private com.prjmanutencao.telas.BadgeButton btnNotificar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblData;
    public static javax.swing.JLabel lblHora;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JMenuItem memAjuSob;
    private javax.swing.JMenuBar men;
    private javax.swing.JMenu menAju;
    public static javax.swing.JMenu menCad;
    private javax.swing.JMenuItem menCadSet;
    private javax.swing.JMenuItem menCadUsu;
    private javax.swing.JMenu menEst;
    private javax.swing.JMenuItem menEstCadAlt;
    private javax.swing.JMenuItem menEstRetProd;
    private javax.swing.JMenu menOpc;
    public static javax.swing.JMenuItem menOpcAce;
    private javax.swing.JMenuItem menOpcSai;
    private javax.swing.JMenu menOs;
    public static javax.swing.JMenuItem menOsAbeFec;
    private javax.swing.JMenuItem menOsAbr;
    private javax.swing.JMenuItem menOsChamados;
    public static javax.swing.JMenuItem menPreAbr;
    private javax.swing.JMenuItem menPreMen;
    public static javax.swing.JMenu menPreSemEnc;
    private javax.swing.JMenuItem menPreSemEnce;
    public static javax.swing.JMenu menRel;
    private javax.swing.JMenuItem menRelOs;
    private javax.swing.JMenuItem menRelPre;
    // End of variables declaration//GEN-END:variables

    /**
     * Classe responsável por implementar a atualização contínua da hora em um
     * rótulo (lblHora) ou um campo de texto (txtHora). A classe utiliza a
     * interface ActionListener para responder a eventos de temporizador.
     */
    class hora implements ActionListener {

        /**
         * Método sobrescrito da interface ActionListener. Atualiza o rótulo
         * "lblHora" com o horário atual formatado no padrão "HH:mm:ss".
         *
         * @param e Evento acionado pelo Timer.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Obtém a hora atual.
            Calendar now = Calendar.getInstance();
            // Atualiza o texto de "lblHora" com o horário formatado.
            lblHora.setText(String.format("%1$tH:%1$tM:%1tS", now));
        }

        /**
         * Método adicional (não está sobrescrevendo ActionListener). Inicializa
         * um Timer que atualiza continuamente o campo de texto "txtHora" com o
         * horário atual. Esse método cria um novo Timer e o inicia.
         *
         * @param e Evento acionado (não utilizado diretamente neste método).
         */
        public void actionPerformed1(ActionEvent e) {
            // Obtém a hora atual.
            Calendar now = Calendar.getInstance();

            // Cria um novo Timer para disparar eventos a cada 1 segundo
            Timer timer = new Timer(1000, new hora());

            // Inicia o Timer.
            timer.start();

        }

    }

    /**
     * Método responsável por definir o ícone principal da aplicação. Utiliza a
     * classe Toolkit para carregar a imagem do recurso especificado.
     */
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png")));
    }
}
