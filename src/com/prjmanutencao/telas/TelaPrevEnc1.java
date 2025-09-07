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
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Classe TelaPrevEnc1
 *
 * Representa uma janela interna (JInternalFrame) responsável pelo encerramento
 * de atividades preventivas. Contém atributos relacionados à conexão com banco
 * de dados, controle de arquivos, identificação de preventiva e parâmetros de
 * acesso FTP.
 *
 * Atributos: - conexao: conexão com o banco de dados. - pst: comando SQL
 * preparado. - rs: conjunto de resultados de uma consulta SQL.
 *
 * - tipo_pre, arq, id_prev1, nome_pre, prev: variáveis de controle da
 * preventiva.
 *
 * - acesso: endereço IP do servidor FTP (estático). - log, sen: credenciais de
 * acesso FTP. - dire: diretório FTP de destino. - file1, file2, file3: nomes de
 * diretórios padrão para diferentes tipos de preventiva hidráulica.
 *
 * - des, nom_arq, end: variáveis auxiliares para descrição, nome de arquivo e
 * caminho.
 *
 * - id4, id5, id6: identificadores estáticos utilizados em diferentes contextos
 * da aplicação. - idPrev1: variável auxiliar para controle de identificador da
 * preventiva.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPrevEnc1 extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String prev = null;

    String tipo_pre = null;
    String arq = null;
    String id_prev = null;
    String nome_pre = null;

    public static String acesso = ("127.0.0.1");
    String log = "servidor";
    String sen = "12345";
    int porta = 21;
    String dire = null;
    String file1 = "hidra_mensal";
    String file2 = "hidra_trimestral";
    String file3 = "hidra_semestral";

    String des = null;
    String nom_arq = null;
    String end = null;

    public static String id4 = null;
    public static String id5 = null;
    public static String id6 = null;

    String idPrev1;

    /**
     * Construtor da classe TelaPrevEnc1
     *
     * Responsável por inicializar a interface gráfica da janela de encerramento
     * de preventiva. Ao ser instanciado, o construtor realiza as seguintes
     * ações:
     *
     * - Chama o método `initComponents()` para configurar os componentes
     * visuais da interface. - Estabelece a conexão com o banco de dados através
     * de `ModuloConexao.conector()`. - Desabilita as três primeiras abas do
     * componente `jtpAbas1`, impedindo a navegação inicial por elas.
     *
     * Essa configuração inicial garante que a interface esteja pronta para uso
     * e que apenas as abas apropriadas sejam acessíveis conforme o fluxo da
     * aplicação.
     */
    public TelaPrevEnc1() {
        initComponents();
        conexao = ModuloConexao.conector();

        jtpAbas1.setEnabledAt(0, false);
        jtpAbas1.setEnabledAt(1, false);
        jtpAbas1.setEnabledAt(2, false);
        
        jScrollPane12.getVerticalScrollBar().setUnitIncrement(25);
        jScrollPane14.getVerticalScrollBar().setUnitIncrement(25);
        jScrollPane17.getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     * Método prev_encerrada3
     *
     * Responsável por carregar os dados de uma preventiva hidráulica mensal
     * encerrada a partir do banco de dados, utilizando o identificador `id4`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_hidraulica_mensal` para recuperar todos os campos relevantes. -
     * Preenche os componentes da interface gráfica com os dados retornados: -
     * Informações gerais: ID, datas, tempo, situação, técnico, setor,
     * equipamento, código. - Itens A119 a A145: situação (`txtPreAxxx`) e
     * observação (`jtaAnoAxxx`). - Tipo de preventiva (`tipo_pre`). - Em caso
     * de erro na consulta, exibe uma mensagem via `JOptionPane`.
     *
     * Observações: - O método assume que o resultado da consulta terá
     * exatamente uma linha. - A estrutura atual é extensa e repetitiva; pode
     * ser otimizada com laços e reflexão.
     *
     * Esse método é essencial para restaurar os dados de uma preventiva
     * hidráulica mensal já encerrada, permitindo visualização e edição na
     * interface.
     */
    public void prev_encerrada3() {

        String sql = "select id_hidraulica_mensal, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27, tipo_prev from form_hidraulica_mensal where id_hidraulica_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id4);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre3.setText(rs.getString(1));
                idPrev1 = (rs.getString(1));
                txtDatIni3.setText(rs.getString(2));
                txtDatFin3.setText(rs.getString(3));
                txtTemPre3.setText(rs.getString(4));
                txtDatPre3.setText(rs.getString(5));
                txtStaPre3.setText(rs.getString(6));
                txtNomTec3.setText(rs.getString(7));
                jtaSetPre3.setText(rs.getString(8));
                jtaEquPre3.setText(rs.getString(9));
                txtCodEqu3.setText(rs.getString(10));

                txtPreA119.setText(rs.getString(11));
                jtaAnoA119.setText(rs.getString(12));

                txtPreA120.setText(rs.getString(13));
                jtaAnoA120.setText(rs.getString(14));

                txtPreA121.setText(rs.getString(15));
                jtaAnoA121.setText(rs.getString(16));

                txtPreA122.setText(rs.getString(17));
                jtaAnoA122.setText(rs.getString(18));

                txtPreA123.setText(rs.getString(19));
                jtaAnoA123.setText(rs.getString(20));

                txtPreA124.setText(rs.getString(21));
                jtaAnoA124.setText(rs.getString(22));

                txtPreA125.setText(rs.getString(23));
                jtaAnoA125.setText(rs.getString(24));

                txtPreA126.setText(rs.getString(25));
                jtaAnoA126.setText(rs.getString(26));

                txtPreA127.setText(rs.getString(27));
                jtaAnoA127.setText(rs.getString(28));

                txtPreA128.setText(rs.getString(29));
                jtaAnoA128.setText(rs.getString(30));

                txtPreA129.setText(rs.getString(31));
                jtaAnoA129.setText(rs.getString(32));

                txtPreA130.setText(rs.getString(33));
                jtaAnoA130.setText(rs.getString(34));

                txtPreA131.setText(rs.getString(35));
                jtaAnoA131.setText(rs.getString(36));

                txtPreA132.setText(rs.getString(37));
                jtaAnoA132.setText(rs.getString(38));

                txtPreA133.setText(rs.getString(39));
                jtaAnoA133.setText(rs.getString(40));

                txtPreA134.setText(rs.getString(41));
                jtaAnoA134.setText(rs.getString(42));

                txtPreA135.setText(rs.getString(43));
                jtaAnoA135.setText(rs.getString(44));

                txtPreA136.setText(rs.getString(45));
                jtaAnoA136.setText(rs.getString(46));

                txtPreA137.setText(rs.getString(47));
                jtaAnoA137.setText(rs.getString(48));

                txtPreA138.setText(rs.getString(49));
                jtaAnoA138.setText(rs.getString(50));

                txtPreA139.setText(rs.getString(51));
                jtaAnoA139.setText(rs.getString(52));

                txtPreA140.setText(rs.getString(53));
                jtaAnoA140.setText(rs.getString(54));

                txtPreA141.setText(rs.getString(55));
                jtaAnoA141.setText(rs.getString(56));

                txtPreA142.setText(rs.getString(57));
                jtaAnoA142.setText(rs.getString(58));

                txtPreA143.setText(rs.getString(59));
                jtaAnoA143.setText(rs.getString(60));

                txtPreA144.setText(rs.getString(61));
                jtaAnoA144.setText(rs.getString(62));

                txtPreA145.setText(rs.getString(63));
                jtaAnoA145.setText(rs.getString(64));

                tipo_pre = (rs.getString(65));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre3
     *
     * Responsável por realizar a pesquisa de arquivos relacionados à preventiva
     * hidráulica mensal encerrada, com base no identificador `id4` e no texto
     * digitado em `txtPesArq`.
     *
     * Funcionalidades: - Monta uma consulta SQL na tabela
     * `arquivos_hidraulica_mensal`, buscando registros que: - Possuam o mesmo
     * `id_hidraulica_mensal` informado. - Tenham `nome_arq` compatível com o
     * padrão `arq + " - " + txtPesArq.getText() + "%"`. - Executa a consulta e
     * atualiza a tabela `tblArqPre` com os resultados usando `DbUtils`. - Chama
     * o método `os_encerrada_quant3()` para atualizar a quantidade de arquivos
     * encontrados. - Em caso de erro na execução da consulta, exibe uma
     * mensagem via `JOptionPane`.
     *
     * Esse método permite ao usuário visualizar os arquivos associados à
     * preventiva hidráulica mensal encerrada, com base em filtros dinâmicos e
     * interação direta com a interface.
     */
    private void arq_pre3() {

        String sql = "select id_hidraulica_mensal as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_hidraulica_mensal where id_hidraulica_mensal = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id4);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant3();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método os_encerrada_quant3
     *
     * Responsável por calcular a quantidade de arquivos associados à preventiva
     * hidráulica mensal encerrada, com base no identificador `id4` e no prefixo
     * do nome do arquivo (`arq`).
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_mensal` para contar os registros que: - Possuam o
     * mesmo `id_hidraulica_mensal`. - Tenham `nome_arq` compatível com o padrão
     * `arq + "%"`. - Atualiza o campo `txtQanArq` com o resultado da contagem.
     * - Em caso de erro na execução da consulta, exibe uma mensagem via
     * `JOptionPane`.
     *
     * Esse método complementa a pesquisa de arquivos (`arq_pre3`), fornecendo
     * ao usuário a quantidade total de arquivos encontrados para o item
     * selecionado.
     */
    private void os_encerrada_quant3() {

        String sql = "select count(*) from arquivos_hidraulica_mensal where id_hidraulica_mensal = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id4);
            pst.setString(2, arq + "%");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQanArq.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método dowload_arq
     *
     * Responsável por recuperar o caminho (endereço) de um arquivo associado à
     * preventiva hidráulica mensal, com base no identificador `id4` e no nome
     * do arquivo informado em `txtFotPre8`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_mensal` para buscar o campo `endere_arq`. - Utiliza
     * `PreparedStatement` para evitar injeção de SQL e garantir segurança na
     * consulta. - Se houver resultado, armazena o endereço do arquivo na
     * variável `end`. - Em caso de erro na execução da consulta, exibe uma
     * mensagem via `JOptionPane`.
     *
     * Esse método é essencial para preparar o caminho do arquivo antes de
     * realizar o download físico ou exibir a imagem correspondente na
     * interface.
     */
    private void dowload_arq() {

        String sql = "select endere_arq from arquivos_hidraulica_mensal where id_hidraulica_mensal = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id4);
            pst.setString(2, txtFotPre8.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                end = (rs.getString(1));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método prev_encerrada4
     *
     * Responsável por carregar os dados de uma preventiva hidráulica trimestral
     * encerrada a partir do banco de dados, utilizando o identificador `id5`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_hidraulica_trimestral` para recuperar todos os campos relevantes. -
     * Preenche os componentes da interface gráfica com os dados retornados: -
     * Informações gerais: ID, datas, tempo, situação, técnico, setor,
     * equipamento, código. - Itens A173 a A202: situação (`txtPreAxxx`) e
     * observação (`jtaAnoAxxx`). - Tipo de preventiva (`tipo_pre`). - Em caso
     * de erro na consulta, exibe uma mensagem via `JOptionPane`.
     *
     * Observações: - O método assume que o resultado da consulta terá
     * exatamente uma linha. - A estrutura atual é extensa e repetitiva; pode
     * ser otimizada com laços e reflexão.
     *
     * Esse método é essencial para restaurar os dados de uma preventiva
     * hidráulica trimestral já encerrada, permitindo visualização e edição na
     * interface.
     */
    public void prev_encerrada4() {

        String sql = "select id_hidraulica_trimestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27, a28, obser28,  a29, obser29,  a30, obser30, tipo_prev from form_hidraulica_trimestral where id_hidraulica_trimestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id5);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre5.setText(rs.getString(1));
                idPrev1 = (rs.getString(1));
                txtDatIni5.setText(rs.getString(2));
                txtDatFin5.setText(rs.getString(3));
                txtTemPre5.setText(rs.getString(4));
                txtDatPre5.setText(rs.getString(5));
                txtStaPre5.setText(rs.getString(6));
                txtNomTec5.setText(rs.getString(7));
                jtaSetPre5.setText(rs.getString(8));
                jtaEquPre5.setText(rs.getString(9));
                txtCodEqu5.setText(rs.getString(10));

                txtPreA173.setText(rs.getString(11));
                jtaAnoA173.setText(rs.getString(12));

                txtPreA174.setText(rs.getString(13));
                jtaAnoA174.setText(rs.getString(14));

                txtPreA175.setText(rs.getString(15));
                jtaAnoA175.setText(rs.getString(16));

                txtPreA176.setText(rs.getString(17));
                jtaAnoA176.setText(rs.getString(18));

                txtPreA177.setText(rs.getString(19));
                jtaAnoA177.setText(rs.getString(20));

                txtPreA178.setText(rs.getString(21));
                jtaAnoA178.setText(rs.getString(22));

                txtPreA179.setText(rs.getString(23));
                jtaAnoA179.setText(rs.getString(24));

                txtPreA180.setText(rs.getString(25));
                jtaAnoA180.setText(rs.getString(26));

                txtPreA181.setText(rs.getString(27));
                jtaAnoA181.setText(rs.getString(28));

                txtPreA182.setText(rs.getString(29));
                jtaAnoA182.setText(rs.getString(30));

                txtPreA183.setText(rs.getString(31));
                jtaAnoA183.setText(rs.getString(32));

                txtPreA184.setText(rs.getString(33));
                jtaAnoA184.setText(rs.getString(34));

                txtPreA185.setText(rs.getString(35));
                jtaAnoA185.setText(rs.getString(36));

                txtPreA186.setText(rs.getString(37));
                jtaAnoA186.setText(rs.getString(38));

                txtPreA187.setText(rs.getString(39));
                jtaAnoA187.setText(rs.getString(40));

                txtPreA188.setText(rs.getString(41));
                jtaAnoA188.setText(rs.getString(42));

                txtPreA189.setText(rs.getString(43));
                jtaAnoA189.setText(rs.getString(44));

                txtPreA190.setText(rs.getString(45));
                jtaAnoA190.setText(rs.getString(46));

                txtPreA191.setText(rs.getString(47));
                jtaAnoA191.setText(rs.getString(48));

                txtPreA192.setText(rs.getString(49));
                jtaAnoA192.setText(rs.getString(50));

                txtPreA193.setText(rs.getString(51));
                jtaAnoA193.setText(rs.getString(52));

                txtPreA194.setText(rs.getString(53));
                jtaAnoA194.setText(rs.getString(54));

                txtPreA195.setText(rs.getString(55));
                jtaAnoA195.setText(rs.getString(56));

                txtPreA196.setText(rs.getString(57));
                jtaAnoA196.setText(rs.getString(58));

                txtPreA197.setText(rs.getString(59));
                jtaAnoA197.setText(rs.getString(60));

                txtPreA198.setText(rs.getString(61));
                jtaAnoA198.setText(rs.getString(62));

                txtPreA199.setText(rs.getString(63));
                jtaAnoA199.setText(rs.getString(64));

                txtPreA200.setText(rs.getString(65));
                jtaAnoA200.setText(rs.getString(66));

                txtPreA201.setText(rs.getString(67));
                jtaAnoA201.setText(rs.getString(68));

                txtPreA202.setText(rs.getString(69));
                jtaAnoA202.setText(rs.getString(70));

                tipo_pre = (rs.getString(71));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre4
     *
     * Responsável por realizar a pesquisa de arquivos relacionados à preventiva
     * hidráulica trimestral encerrada, com base no identificador `id5` e no
     * texto digitado em `txtPesArq`.
     *
     * Funcionalidades: - Monta uma consulta SQL na tabela
     * `arquivos_hidraulica_trimestral`, buscando registros que: - Possuam o
     * mesmo `id_hidraulica_trimestral` informado. - Tenham `nome_arq`
     * compatível com o padrão `arq + " - " + txtPesArq.getText() + "%"`. -
     * Executa a consulta e atualiza a tabela `tblArqPre` com os resultados
     * usando `DbUtils`. - Chama o método `os_encerrada_quant4()` para atualizar
     * a quantidade de arquivos encontrados. - Em caso de erro na execução da
     * consulta, exibe uma mensagem via `JOptionPane`.
     *
     * Esse método permite ao usuário visualizar os arquivos associados à
     * preventiva hidráulica trimestral encerrada, com base em filtros dinâmicos
     * e interação direta com a interface.
     */
    private void arq_pre4() {

        String sql = "select id_hidraulica_trimestral as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_hidraulica_trimestral where id_hidraulica_trimestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id5);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant4();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método os_encerrada_quant4
     *
     * Responsável por calcular a quantidade de arquivos associados à preventiva
     * hidráulica trimestral encerrada, com base no identificador `id5` e no
     * prefixo do nome do arquivo (`arq`).
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_trimestral` para contar os registros que: - Possuam
     * o mesmo `id_hidraulica_trimestral`. - Tenham `nome_arq` compatível com o
     * padrão `arq + "%"`. - Atualiza o campo `txtQanArq` com o resultado da
     * contagem. - Em caso de erro na execução da consulta, exibe uma mensagem
     * via `JOptionPane`.
     *
     * Esse método complementa a pesquisa de arquivos (`arq_pre4`), fornecendo
     * ao usuário a quantidade total de arquivos encontrados para o item
     * selecionado.
     */
    private void os_encerrada_quant4() {

        String sql = "select count(*) from arquivos_hidraulica_trimestral where id_hidraulica_trimestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id5);
            pst.setString(2, arq + "%");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQanArq.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método dowload_arq1
     *
     * Responsável por recuperar o caminho (endereço) de um arquivo associado à
     * preventiva hidráulica trimestral, com base no identificador `id5` e no
     * nome do arquivo informado em `txtFotPre8`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_trimestral` para buscar o campo `endere_arq`. -
     * Utiliza `PreparedStatement` para evitar injeção de SQL e garantir
     * segurança na consulta. - Se houver resultado, armazena o endereço do
     * arquivo na variável `end`. - Em caso de erro na execução da consulta,
     * exibe uma mensagem via `JOptionPane`.
     *
     * Esse método é essencial para preparar o caminho do arquivo antes de
     * realizar o download físico ou exibir a imagem correspondente na
     * interface.
     */
    private void dowload_arq1() {

        String sql = "select endere_arq from arquivos_hidraulica_trimestral where id_hidraulica_trimestral = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id5);
            pst.setString(2, txtFotPre8.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                end = (rs.getString(1));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método prev_encerrada5
     *
     * Responsável por carregar os dados de uma preventiva hidráulica semestral
     * encerrada a partir do banco de dados, utilizando o identificador `id6`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_hidraulica_semestral` para recuperar todos os campos relevantes. -
     * Preenche os componentes da interface gráfica com os dados retornados: -
     * Informações gerais: ID, datas, tempo, situação, técnico, setor,
     * equipamento, código. - Itens A203 a A235: situação (`txtPreAxxx`) e
     * observação (`jtaAnoAxxx`). - Tipo de preventiva (`tipo_pre`). - Em caso
     * de erro na consulta, exibe uma mensagem via `JOptionPane`.
     *
     * Observações: - O método assume que o resultado da consulta terá
     * exatamente uma linha. - A estrutura atual é extensa e repetitiva; pode
     * ser otimizada com laços e reflexão.
     *
     * Esse método é essencial para restaurar os dados de uma preventiva
     * hidráulica semestral já encerrada, permitindo visualização e edição na
     * interface.
     */
    public void prev_encerrada5() {

        String sql = "select id_hidraulica_semestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27, a28, obser28,  a29, obser29,  a30, obser30, a31, obser31,  a32, obser32, a33, obser33, tipo_prev from form_hidraulica_semestral where id_hidraulica_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id6);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre6.setText(rs.getString(1));
                idPrev1 = (rs.getString(1));
                txtDatIni6.setText(rs.getString(2));
                txtDatFin6.setText(rs.getString(3));
                txtTemPre6.setText(rs.getString(4));
                txtDatPre6.setText(rs.getString(5));
                txtStaPre6.setText(rs.getString(6));
                txtNomTec6.setText(rs.getString(7));
                jtaSetPre6.setText(rs.getString(8));
                jtaEquPre6.setText(rs.getString(9));
                txtCodEqu6.setText(rs.getString(10));

                txtPreA203.setText(rs.getString(11));
                jtaAnoA203.setText(rs.getString(12));

                txtPreA204.setText(rs.getString(13));
                jtaAnoA204.setText(rs.getString(14));

                txtPreA205.setText(rs.getString(15));
                jtaAnoA205.setText(rs.getString(16));

                txtPreA206.setText(rs.getString(17));
                jtaAnoA206.setText(rs.getString(18));

                txtPreA207.setText(rs.getString(19));
                jtaAnoA207.setText(rs.getString(20));

                txtPreA208.setText(rs.getString(21));
                jtaAnoA208.setText(rs.getString(22));

                txtPreA209.setText(rs.getString(23));
                jtaAnoA209.setText(rs.getString(24));

                txtPreA210.setText(rs.getString(25));
                jtaAnoA210.setText(rs.getString(26));

                txtPreA211.setText(rs.getString(27));
                jtaAnoA211.setText(rs.getString(28));

                txtPreA212.setText(rs.getString(29));
                jtaAnoA212.setText(rs.getString(30));

                txtPreA213.setText(rs.getString(31));
                jtaAnoA213.setText(rs.getString(32));

                txtPreA214.setText(rs.getString(33));
                jtaAnoA214.setText(rs.getString(34));

                txtPreA215.setText(rs.getString(35));
                jtaAnoA215.setText(rs.getString(36));

                txtPreA216.setText(rs.getString(37));
                jtaAnoA216.setText(rs.getString(38));

                txtPreA217.setText(rs.getString(39));
                jtaAnoA217.setText(rs.getString(40));

                txtPreA218.setText(rs.getString(41));
                jtaAnoA218.setText(rs.getString(42));

                txtPreA219.setText(rs.getString(43));
                jtaAnoA219.setText(rs.getString(44));

                txtPreA220.setText(rs.getString(45));
                jtaAnoA220.setText(rs.getString(46));

                txtPreA221.setText(rs.getString(47));
                jtaAnoA221.setText(rs.getString(48));

                txtPreA222.setText(rs.getString(49));
                jtaAnoA222.setText(rs.getString(50));

                txtPreA223.setText(rs.getString(51));
                jtaAnoA223.setText(rs.getString(52));

                txtPreA224.setText(rs.getString(53));
                jtaAnoA224.setText(rs.getString(54));

                txtPreA225.setText(rs.getString(55));
                jtaAnoA225.setText(rs.getString(56));

                txtPreA226.setText(rs.getString(57));
                jtaAnoA226.setText(rs.getString(58));

                txtPreA227.setText(rs.getString(59));
                jtaAnoA227.setText(rs.getString(60));

                txtPreA228.setText(rs.getString(61));
                jtaAnoA228.setText(rs.getString(62));

                txtPreA229.setText(rs.getString(63));
                jtaAnoA229.setText(rs.getString(64));

                txtPreA230.setText(rs.getString(65));
                jtaAnoA230.setText(rs.getString(66));

                txtPreA231.setText(rs.getString(67));
                jtaAnoA231.setText(rs.getString(68));

                txtPreA232.setText(rs.getString(69));
                jtaAnoA232.setText(rs.getString(70));

                txtPreA233.setText(rs.getString(71));
                jtaAnoA233.setText(rs.getString(72));

                txtPreA234.setText(rs.getString(73));
                jtaAnoA234.setText(rs.getString(74));

                txtPreA235.setText(rs.getString(75));
                jtaAnoA235.setText(rs.getString(76));

                tipo_pre = (rs.getString(77));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre5
     *
     * Responsável por realizar a pesquisa de arquivos relacionados à preventiva
     * hidráulica semestral encerrada, com base no identificador `id6` e no
     * texto digitado em `txtPesArq`.
     *
     * Funcionalidades: - Monta uma consulta SQL na tabela
     * `arquivos_hidraulica_semestral`, buscando registros que: - Possuam o
     * mesmo `id_hidraulica_semestral` informado. - Tenham `nome_arq` compatível
     * com o padrão `arq + " - " + txtPesArq.getText() + "%"`. - Executa a
     * consulta e atualiza a tabela `tblArqPre` com os resultados usando
     * `DbUtils`. - Chama o método `os_encerrada_quant5()` para atualizar a
     * quantidade de arquivos encontrados. - Em caso de erro na execução da
     * consulta, exibe uma mensagem via `JOptionPane`.
     *
     * Esse método permite ao usuário visualizar os arquivos associados à
     * preventiva hidráulica semestral encerrada, com base em filtros dinâmicos
     * e interação direta com a interface.
     */
    private void arq_pre5() {

        String sql = "select id_hidraulica_semestral as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_hidraulica_semestral where id_hidraulica_semestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id6);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant5();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método os_encerrada_quant5
     *
     * Responsável por calcular a quantidade de arquivos associados à preventiva
     * hidráulica semestral encerrada, com base no identificador `id6` e no
     * prefixo do nome do arquivo (`arq`).
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_semestral` para contar os registros que: - Possuam o
     * mesmo `id_hidraulica_semestral`. - Tenham `nome_arq` compatível com o
     * padrão `arq + "%"`. - Atualiza o campo `txtQanArq` com o resultado da
     * contagem. - Em caso de erro na execução da consulta, exibe uma mensagem
     * via `JOptionPane`.
     *
     * Esse método complementa a pesquisa de arquivos (`arq_pre5`), fornecendo
     * ao usuário a quantidade total de arquivos encontrados para o item
     * selecionado.
     */
    private void os_encerrada_quant5() {

        String sql = "select count(*) from arquivos_hidraulica_semestral where id_hidraulica_semestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id6);
            pst.setString(2, arq + "%");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQanArq.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método dowload_arq2
     *
     * Responsável por recuperar o caminho (endereço) de um arquivo associado à
     * preventiva hidráulica semestral, com base no identificador `id6` e no
     * nome do arquivo informado em `txtFotPre8`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `arquivos_hidraulica_semestral` para buscar o campo `endere_arq`. -
     * Utiliza `PreparedStatement` para evitar injeção de SQL e garantir
     * segurança na consulta. - Se houver resultado, armazena o endereço do
     * arquivo na variável `end`. - Em caso de erro na execução da consulta,
     * exibe uma mensagem via `JOptionPane`.
     *
     * Esse método é essencial para preparar o caminho do arquivo antes de
     * realizar o download físico ou exibir a imagem correspondente na
     * interface.
     */
    private void dowload_arq2() {

        String sql = "select endere_arq from arquivos_hidraulica_semestral where id_hidraulica_semestral = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id6);
            pst.setString(2, txtFotPre8.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                end = (rs.getString(1));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método setar_arq
     *
     * Responsável por capturar os dados do arquivo selecionado na tabela
     * `tblArqPre` e iniciar os processos de visualização e download.
     *
     * Funcionalidades: - Obtém o índice da linha selecionada na tabela
     * (`getSelectedRow()`). - Recupera o nome do arquivo (`coluna 1`) e o ID da
     * preventiva (`coluna 0`) da linha selecionada. - Atualiza os campos
     * `txtFotPre8`, `nome_pre` e `id_prev` com os valores obtidos. - Exibe o ID
     * da preventiva no console para fins de depuração. - Chama os métodos
     * `arq_fotos()` e `carregarArquivo()` para exibir a imagem e preparar o
     * download.
     *
     * Esse método é essencial para permitir que o usuário interaja com os
     * arquivos listados, tornando a navegação e manipulação dos dados mais
     * fluida e intuitiva.
     */
    private void setar_arq() {

        int setar = tblArqPre.getSelectedRow();
        txtFotPre8.setText(tblArqPre.getModel().getValueAt(setar, 1).toString());
        nome_pre = (tblArqPre.getModel().getValueAt(setar, 1).toString());
        id_prev = (tblArqPre.getModel().getValueAt(setar, 0).toString());
        arq_fotos();
        carregarArquivo();
        
    }

    /**
     * Método salve_file
     *
     * Responsável por permitir ao usuário salvar um arquivo selecionado da
     * preventiva hidráulica, definindo o nome e o local de destino via
     * `JFileChooser`.
     *
     * Funcionalidades: - Obtém o nome da foto a partir de `txtFotPre8`. - Cria
     * um `JFileChooser` configurado para salvar arquivos. - Sugere um nome de
     * arquivo sem extensão como padrão. - Se o usuário confirmar o salvamento:
     * - Recupera a extensão original do arquivo (`fe`) a partir de `end`. -
     * Define o caminho de destino (`des`) com a extensão preservada. - Chama
     * `download_file()` para realizar o salvamento físico. - Limpa os campos da
     * interface e oculta o painel de arquivos. - Exibe mensagem de sucesso. -
     * Se o usuário cancelar, define `des` como `null`. - Em caso de erro, exibe
     * mensagem via `JOptionPane`.
     *
     * Observações: - O método preserva a extensão original do arquivo, mesmo
     * que o usuário não a informe. - A limpeza da interface após o salvamento
     * garante uma experiência fluida e sem resíduos.
     *
     * Esse método oferece ao usuário controle total sobre o destino do arquivo,
     * garantindo segurança e clareza no processo de salvamento.
     */
    private void salve_file() {

        String nome_foto = (txtFotPre8.getText());
        String fe = "";
        String fileName = end;

        try {

            JFileChooser file = new JFileChooser();
            file.setDialogTitle("Salvar Arquivo");
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file.setSelectedFile(new File(nome_foto.replaceFirst("[.][^.]+$", "")));
            int i = file.showSaveDialog(null);
            if (i == 1) {
                des = null;
            } else {
                int a = fileName.lastIndexOf('.');
                if (a >= 0) {
                    fe = fileName.substring(a + 1);
                }
                end = (file.getSelectedFile().getName());
                File arquivo = file.getSelectedFile();
                des = arquivo.getPath() + (".") + fe;
                download_file();
                jpaArqPre.setVisible(false);
                lblFotEltMen8.setIcon(null);
                txtFotPre8.setText(null);
                btnSalFotPre8.setEnabled(false);
                ((DefaultTableModel) tblArqPre.getModel()).setRowCount(0);
                txtPesArq.setText(null);
                txtQanArq.setText(null);
                JOptionPane.showMessageDialog(null, "Dowload Feito com Sucesso!");
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método download_file
     *
     * Responsável por realizar o download de um arquivo via protocolo FTP e
     * exibir a imagem correspondente na interface.
     *
     * Etapas executadas: - Cria uma instância de `FTPClient` e conecta ao
     * servidor FTP usando o endereço `acesso`. - Realiza login com as
     * credenciais `log` e `sen`. - Navega até o diretório de trabalho
     * correspondente à preventiva (`dire + "/" + idPrev1`). - Configura o modo
     * passivo e o tipo de arquivo como binário. - Cria um `FileOutputStream`
     * para salvar o arquivo como `.jpg` no caminho `des`. - Tenta recuperar o
     * arquivo com `ftp.retrieveFile(nome_pre, fos)`: - Se bem-sucedido, carrega
     * a imagem e a exibe no componente `lblFotEltMen8`, redimensionando-a. -
     * Habilita o botão `btnSalFotPre8` para permitir salvar a imagem. - Se
     * falhar, exibe uma mensagem de erro. - Fecha o `FileOutputStream`.
     *
     * Em caso de exceção de I/O, exibe uma mensagem de erro via `JOptionPane`.
     *
     * Esse método é essencial para integrar o processo de download com a
     * visualização da imagem na interface gráfica.
     */
    private void download_file() {

        FTPClient ftp = new FTPClient();

        try {

            ftp.connect(acesso, porta);

            ftp.login(log, sen);

            ftp.changeWorkingDirectory(dire + "/" + idPrev1);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            FileOutputStream fos = new FileOutputStream(des + ".jpg");
            if (ftp.retrieveFile(nome_pre, fos)) {

                ImageIcon icon = new ImageIcon(des + ".jpg");
                icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen8.getWidth(), lblFotEltMen8.getHeight(), Image.SCALE_SMOOTH));
                lblFotEltMen8.setIcon(icon);
                btnSalFotPre8.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Carregar o Arquivo.");

            }
            fos.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * Método carregarArquivo
     *
     * Finalidade: Recupera um arquivo via FTP e exibe sua imagem diretamente na
     * interface gráfica, sem necessidade de salvamento local.
     *
     * Funcionalidades: - Conecta ao servidor FTP usando os dados de acesso
     * (`acesso`, `porta`, `log`, `sen`). - Define modo passivo e tipo de
     * arquivo binário para transferência. - Monta o caminho do arquivo com base
     * em `dire`, `idPrev` e `nome_pre`. - Recupera o arquivo como um fluxo de
     * bytes (`ByteArrayOutputStream`). - Converte os bytes em `InputStream` e
     * carrega como imagem (`ImageIcon`). - Redimensiona a imagem para se
     * ajustar ao componente `lblFotEltMen8`. - Exibe a imagem na interface e
     * habilita o botão de salvamento (`btnSalFotPre8`). - Encerra a conexão com
     * o servidor FTP.
     *
     * Observações: - O método assume que o arquivo é uma imagem compatível com
     * `ImageIO.read()`. - Em caso de erro (conexão, leitura ou exibição), exibe
     * mensagem via `JOptionPane`.
     */
    private void carregarArquivo() {

        FTPClient ftpCliente = new FTPClient();
        try {
            // Conecta ao servidor FTP
            ftpCliente.connect(acesso, porta);

            ftpCliente.login(log, sen);
            ftpCliente.enterLocalPassiveMode(); // Modo passivo
            ftpCliente.setFileType(FTP.BINARY_FILE_TYPE); // Tipo de arquivo binário

            // Arquivo que deseja recuperar
            String caminhoArquivo = dire.concat("/").concat(idPrev1).concat("/").concat(nome_pre);

            // Recupera o arquivo para um ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ftpCliente.retrieveFile(caminhoArquivo, outputStream);

            // Cria um InputStream a partir do ByteArrayOutputStream
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // Aqui você pode usar o InputStream para exibir ou processar o arquivo sem salvá-lo
            // Por exemplo: carregar uma imagem em um componente de interface gráfica
            ImageIcon icon = new ImageIcon(ImageIO.read(inputStream));
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen8.getWidth(), lblFotEltMen8.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen8.setIcon(icon);
            btnSalFotPre8.setEnabled(true);

            // Desconecta do servidor
            ftpCliente.logout();
            ftpCliente.disconnect();
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método pesq
     *
     * Responsável por direcionar a pesquisa de arquivos conforme o tipo de
     * preventiva hidráulica selecionada.
     *
     * Funcionalidades: - Verifica o valor da variável `tipo_pre` e chama o
     * método correspondente: - "Hidráulica - Mensal" → `arq_pre3()` -
     * "Hidráulica - Trimestral" → `arq_pre4()` - "Hidráulica - Semestral" →
     * `arq_pre5()`
     *
     * Esse método centraliza a lógica de pesquisa, tornando o código mais
     * organizado e fácil de manter.
     */
    private void pesq() {

        if (tipo_pre.equals("Hidráulica - Mensal")) {
            arq_pre3();
        }
        if (tipo_pre.equals("Hidráulica - Trimestral")) {
            arq_pre4();
        }
        if (tipo_pre.equals("Hidráulica - Semestral")) {
            arq_pre5();
        }
    }

    private void arq_fotos() {

        if (tipo_pre.equals("Hidráulica - Mensal")) {
            dowload_arq();
            dire = file1;
        }
        if (tipo_pre.equals("Hidráulica - Trimestral")) {
            dowload_arq1();
            dire = file2;
        }
        if (tipo_pre.equals("Hidráulica - Semestral")) {
            dowload_arq2();
            dire = file3;
        }

    }

    /**
     * Método imprimir
     *
     * Responsável por gerar e exibir o relatório da preventiva hidráulica
     * conforme o tipo selecionado.
     *
     * Funcionalidades: - Exibe uma caixa de diálogo para confirmar a impressão.
     * - Se confirmado: - Carrega os arquivos `.jasper` correspondentes aos
     * tipos: mensal, trimestral e semestral. - Cria um mapa de parâmetros
     * (`filtro`) com o ID da preventiva. - Preenche o relatório com
     * `JasperFillManager.fillReport(...)`. - Exibe o relatório com
     * `JasperViewer`, definindo título e ícone da janela. - Em caso de erro
     * (formato inválido ou falha no Jasper), exibe mensagem via `JOptionPane`.
     *
     * Observações: - Os arquivos `.jasper` devem estar corretamente empacotados
     * no diretório `/imprimir`. - O ícone da janela é carregado de
     * `/com/prjmanutencao/icones/iconPrincipal.png`. - O método depende da
     * variável `prev` para determinar o tipo de preventiva.
     *
     * Esse método oferece uma experiência de impressão clara e personalizada,
     * adaptando-se ao tipo de preventiva selecionado pelo usuário.
     */
    private void imprimir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            try {

                InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/HidMen.jasper");
                InputStream caminhoRelativo1 = getClass().getResourceAsStream("/imprimir/HidTri.jasper");
                InputStream caminhoRelativo2 = getClass().getResourceAsStream("/imprimir/HidSem.jasper");

                Map<String, Object> filtro = new HashMap<>();
                JasperPrint print;
                JasperViewer viewer;
                Image icone;

                switch (prev) {
                    case "mensal":
                        filtro.put("id", Integer.parseInt(txtIdPre3.getText()));
                        print = JasperFillManager.fillReport(caminhoRelativo, filtro, conexao);
                        viewer = new JasperViewer(print, false);
                        // Define o título da janela
                        viewer.setTitle("Imprimir");
                        icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                        viewer.setIconImage(icone);
                        viewer.setVisible(true);
                        break;

                    case "trimestral":
                        filtro.put("id", Integer.parseInt(txtIdPre5.getText()));
                        print = JasperFillManager.fillReport(caminhoRelativo1, filtro, conexao);
                        viewer = new JasperViewer(print, false);
                        // Define o título da janela
                        viewer.setTitle("Imprimir");
                        icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                        viewer.setIconImage(icone);
                        viewer.setVisible(true);
                        break;

                    case "semestral":
                        filtro.put("id", Integer.parseInt(txtIdPre6.getText()));
                        print = JasperFillManager.fillReport(caminhoRelativo2, filtro, conexao);
                        viewer = new JasperViewer(print, false);
                        // Define o título da janela
                        viewer.setTitle("Imprimir");
                        icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                        viewer.setIconImage(icone);
                        viewer.setVisible(true);
                        break;

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

        jpaArqPre = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblArqPre = new javax.swing.JTable();
        lblFotEltMen8 = new javax.swing.JLabel();
        txtFotPre8 = new javax.swing.JTextField();
        btnSalFotPre8 = new javax.swing.JButton();
        txtCanFot = new javax.swing.JButton();
        txtPesArq = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtQanArq = new javax.swing.JTextField();
        jtpAbas1 = new javax.swing.JTabbedPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        jpaAba4 = new javax.swing.JPanel();
        txtIdPre3 = new javax.swing.JTextField();
        txtDatIni3 = new javax.swing.JTextField();
        txtDatFin3 = new javax.swing.JTextField();
        txtTemPre3 = new javax.swing.JTextField();
        txtDatPre3 = new javax.swing.JTextField();
        txtNomTec3 = new javax.swing.JTextField();
        jScrollPane15 = new javax.swing.JScrollPane();
        jtaEquPre3 = new javax.swing.JTextArea();
        jScrollPane16 = new javax.swing.JScrollPane();
        jtaSetPre3 = new javax.swing.JTextArea();
        txtCodEqu3 = new javax.swing.JTextField();
        txtStaPre3 = new javax.swing.JTextField();
        jtbAnoPreA119 = new javax.swing.JToggleButton();
        btnFotPreA119 = new javax.swing.JButton();
        txtPreA119 = new javax.swing.JTextField();
        jspAnoPreA119 = new javax.swing.JScrollPane();
        jtaAnoA119 = new javax.swing.JTextArea();
        jspAnoPreA120 = new javax.swing.JScrollPane();
        jtaAnoA120 = new javax.swing.JTextArea();
        txtPreA120 = new javax.swing.JTextField();
        jtbAnoPreA120 = new javax.swing.JToggleButton();
        btnFotPreA120 = new javax.swing.JButton();
        txtPreA121 = new javax.swing.JTextField();
        jtbAnoPreA121 = new javax.swing.JToggleButton();
        btnFotPreA121 = new javax.swing.JButton();
        jspAnoPreA121 = new javax.swing.JScrollPane();
        jtaAnoA121 = new javax.swing.JTextArea();
        txtPreA122 = new javax.swing.JTextField();
        jtbAnoPreA122 = new javax.swing.JToggleButton();
        btnFotPreA122 = new javax.swing.JButton();
        jspAnoPreA122 = new javax.swing.JScrollPane();
        jtaAnoA122 = new javax.swing.JTextArea();
        txtPreA123 = new javax.swing.JTextField();
        jtbAnoPreA123 = new javax.swing.JToggleButton();
        btnFotPreA123 = new javax.swing.JButton();
        jspAnoPreA123 = new javax.swing.JScrollPane();
        jtaAnoA123 = new javax.swing.JTextArea();
        txtPreA124 = new javax.swing.JTextField();
        jtbAnoPreA124 = new javax.swing.JToggleButton();
        btnFotPreA124 = new javax.swing.JButton();
        jspAnoPreA124 = new javax.swing.JScrollPane();
        jtaAnoA124 = new javax.swing.JTextArea();
        txtPreA125 = new javax.swing.JTextField();
        jtbAnoPreA125 = new javax.swing.JToggleButton();
        btnFotPreA125 = new javax.swing.JButton();
        jspAnoPreA125 = new javax.swing.JScrollPane();
        jtaAnoA125 = new javax.swing.JTextArea();
        txtPreA126 = new javax.swing.JTextField();
        jtbAnoPreA126 = new javax.swing.JToggleButton();
        btnFotPreA126 = new javax.swing.JButton();
        jspAnoPreA126 = new javax.swing.JScrollPane();
        jtaAnoA126 = new javax.swing.JTextArea();
        txtPreA127 = new javax.swing.JTextField();
        jspAnoPreA127 = new javax.swing.JScrollPane();
        jtaAnoA127 = new javax.swing.JTextArea();
        jtbAnoPreA127 = new javax.swing.JToggleButton();
        btnFotPreA127 = new javax.swing.JButton();
        txtPreA128 = new javax.swing.JTextField();
        jtbAnoPreA128 = new javax.swing.JToggleButton();
        btnFotPreA128 = new javax.swing.JButton();
        jspAnoPreA128 = new javax.swing.JScrollPane();
        jtaAnoA128 = new javax.swing.JTextArea();
        txtPreA129 = new javax.swing.JTextField();
        jtbAnoPreA129 = new javax.swing.JToggleButton();
        btnFotPreA129 = new javax.swing.JButton();
        jspAnoPreA129 = new javax.swing.JScrollPane();
        jtaAnoA129 = new javax.swing.JTextArea();
        txtPreA130 = new javax.swing.JTextField();
        jtbAnoPreA130 = new javax.swing.JToggleButton();
        btnFotPreA130 = new javax.swing.JButton();
        jspAnoPreA130 = new javax.swing.JScrollPane();
        jtaAnoA130 = new javax.swing.JTextArea();
        txtPreA131 = new javax.swing.JTextField();
        jtbAnoPreA131 = new javax.swing.JToggleButton();
        btnFotPreA131 = new javax.swing.JButton();
        jspAnoPreA131 = new javax.swing.JScrollPane();
        jtaAnoA131 = new javax.swing.JTextArea();
        txtPreA132 = new javax.swing.JTextField();
        jtbAnoPreA132 = new javax.swing.JToggleButton();
        btnFotPreA132 = new javax.swing.JButton();
        jspAnoPreA132 = new javax.swing.JScrollPane();
        jtaAnoA132 = new javax.swing.JTextArea();
        txtPreA133 = new javax.swing.JTextField();
        jtbAnoPreA133 = new javax.swing.JToggleButton();
        btnFotPreA133 = new javax.swing.JButton();
        jspAnoPreA133 = new javax.swing.JScrollPane();
        jtaAnoA133 = new javax.swing.JTextArea();
        txtPreA134 = new javax.swing.JTextField();
        jtbAnoPreA134 = new javax.swing.JToggleButton();
        btnFotPreA134 = new javax.swing.JButton();
        jspAnoPreA134 = new javax.swing.JScrollPane();
        jtaAnoA134 = new javax.swing.JTextArea();
        jspAnoPreA135 = new javax.swing.JScrollPane();
        jtaAnoA135 = new javax.swing.JTextArea();
        jspAnoPreA136 = new javax.swing.JScrollPane();
        jtaAnoA136 = new javax.swing.JTextArea();
        jtbAnoPreA135 = new javax.swing.JToggleButton();
        btnFotPreA135 = new javax.swing.JButton();
        txtPreA135 = new javax.swing.JTextField();
        txtPreA136 = new javax.swing.JTextField();
        jtbAnoPreA136 = new javax.swing.JToggleButton();
        btnFotPreA136 = new javax.swing.JButton();
        txtPreA137 = new javax.swing.JTextField();
        jtbAnoPreA137 = new javax.swing.JToggleButton();
        btnFotPreA137 = new javax.swing.JButton();
        jspAnoPreA137 = new javax.swing.JScrollPane();
        jtaAnoA137 = new javax.swing.JTextArea();
        txtPreA138 = new javax.swing.JTextField();
        jtbAnoPreA138 = new javax.swing.JToggleButton();
        btnFotPreA138 = new javax.swing.JButton();
        jspAnoPreA138 = new javax.swing.JScrollPane();
        jtaAnoA138 = new javax.swing.JTextArea();
        txtPreA139 = new javax.swing.JTextField();
        jtbAnoPreA139 = new javax.swing.JToggleButton();
        btnFotPreA139 = new javax.swing.JButton();
        jspAnoPreA139 = new javax.swing.JScrollPane();
        jtaAnoA139 = new javax.swing.JTextArea();
        txtPreA140 = new javax.swing.JTextField();
        jtbAnoPreA140 = new javax.swing.JToggleButton();
        btnFotPreA140 = new javax.swing.JButton();
        jspAnoPreA140 = new javax.swing.JScrollPane();
        jtaAnoA140 = new javax.swing.JTextArea();
        txtPreA141 = new javax.swing.JTextField();
        jtbAnoPreA141 = new javax.swing.JToggleButton();
        btnFotPreA141 = new javax.swing.JButton();
        jspAnoPreA141 = new javax.swing.JScrollPane();
        jtaAnoA141 = new javax.swing.JTextArea();
        txtPreA142 = new javax.swing.JTextField();
        jtbAnoPreA142 = new javax.swing.JToggleButton();
        btnFotPreA142 = new javax.swing.JButton();
        jspAnoPreA142 = new javax.swing.JScrollPane();
        jtaAnoA142 = new javax.swing.JTextArea();
        txtPreA143 = new javax.swing.JTextField();
        jspAnoPreA143 = new javax.swing.JScrollPane();
        jtaAnoA143 = new javax.swing.JTextArea();
        jtbAnoPreA143 = new javax.swing.JToggleButton();
        btnFotPreA143 = new javax.swing.JButton();
        txtPreA144 = new javax.swing.JTextField();
        jtbAnoPreA144 = new javax.swing.JToggleButton();
        btnFotPreA144 = new javax.swing.JButton();
        jspAnoPreA144 = new javax.swing.JScrollPane();
        jtaAnoA144 = new javax.swing.JTextArea();
        txtPreA145 = new javax.swing.JTextField();
        jtbAnoPreA145 = new javax.swing.JToggleButton();
        btnFotPreA145 = new javax.swing.JButton();
        jspAnoPreA145 = new javax.swing.JScrollPane();
        jtaAnoA145 = new javax.swing.JTextArea();
        btnImpPre3 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jpaAba6 = new javax.swing.JPanel();
        txtIdPre5 = new javax.swing.JTextField();
        txtDatIni5 = new javax.swing.JTextField();
        txtDatFin5 = new javax.swing.JTextField();
        txtTemPre5 = new javax.swing.JTextField();
        txtDatPre5 = new javax.swing.JTextField();
        txtNomTec5 = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        jtaEquPre5 = new javax.swing.JTextArea();
        jScrollPane20 = new javax.swing.JScrollPane();
        jtaSetPre5 = new javax.swing.JTextArea();
        txtCodEqu5 = new javax.swing.JTextField();
        txtStaPre5 = new javax.swing.JTextField();
        jtbAnoPreA173 = new javax.swing.JToggleButton();
        btnFotPreA173 = new javax.swing.JButton();
        txtPreA173 = new javax.swing.JTextField();
        jspAnoPreA173 = new javax.swing.JScrollPane();
        jtaAnoA173 = new javax.swing.JTextArea();
        jspAnoPreA174 = new javax.swing.JScrollPane();
        jtaAnoA174 = new javax.swing.JTextArea();
        txtPreA174 = new javax.swing.JTextField();
        jtbAnoPreA174 = new javax.swing.JToggleButton();
        btnFotPreA174 = new javax.swing.JButton();
        txtPreA175 = new javax.swing.JTextField();
        jtbAnoPreA175 = new javax.swing.JToggleButton();
        btnFotPreA175 = new javax.swing.JButton();
        jspAnoPreA175 = new javax.swing.JScrollPane();
        jtaAnoA175 = new javax.swing.JTextArea();
        txtPreA176 = new javax.swing.JTextField();
        jtbAnoPreA176 = new javax.swing.JToggleButton();
        btnFotPreA176 = new javax.swing.JButton();
        jspAnoPreA176 = new javax.swing.JScrollPane();
        jtaAnoA176 = new javax.swing.JTextArea();
        txtPreA177 = new javax.swing.JTextField();
        jtbAnoPreA177 = new javax.swing.JToggleButton();
        btnFotPreA177 = new javax.swing.JButton();
        jspAnoPreA177 = new javax.swing.JScrollPane();
        jtaAnoA177 = new javax.swing.JTextArea();
        txtPreA178 = new javax.swing.JTextField();
        jtbAnoPreA178 = new javax.swing.JToggleButton();
        btnFotPreA178 = new javax.swing.JButton();
        jspAnoPreA178 = new javax.swing.JScrollPane();
        jtaAnoA178 = new javax.swing.JTextArea();
        txtPreA179 = new javax.swing.JTextField();
        jtbAnoPreA179 = new javax.swing.JToggleButton();
        btnFotPreA179 = new javax.swing.JButton();
        jspAnoPreA179 = new javax.swing.JScrollPane();
        jtaAnoA179 = new javax.swing.JTextArea();
        txtPreA180 = new javax.swing.JTextField();
        jtbAnoPreA180 = new javax.swing.JToggleButton();
        btnFotPreA180 = new javax.swing.JButton();
        jspAnoPreA180 = new javax.swing.JScrollPane();
        jtaAnoA180 = new javax.swing.JTextArea();
        txtPreA181 = new javax.swing.JTextField();
        jspAnoPreA181 = new javax.swing.JScrollPane();
        jtaAnoA181 = new javax.swing.JTextArea();
        jtbAnoPreA181 = new javax.swing.JToggleButton();
        btnFotPreA181 = new javax.swing.JButton();
        txtPreA182 = new javax.swing.JTextField();
        jtbAnoPreA182 = new javax.swing.JToggleButton();
        btnFotPreA182 = new javax.swing.JButton();
        jspAnoPreA182 = new javax.swing.JScrollPane();
        jtaAnoA182 = new javax.swing.JTextArea();
        txtPreA183 = new javax.swing.JTextField();
        jtbAnoPreA183 = new javax.swing.JToggleButton();
        btnFotPreA183 = new javax.swing.JButton();
        jspAnoPreA183 = new javax.swing.JScrollPane();
        jtaAnoA183 = new javax.swing.JTextArea();
        txtPreA184 = new javax.swing.JTextField();
        jtbAnoPreA184 = new javax.swing.JToggleButton();
        btnFotPreA184 = new javax.swing.JButton();
        jspAnoPreA184 = new javax.swing.JScrollPane();
        jtaAnoA184 = new javax.swing.JTextArea();
        txtPreA185 = new javax.swing.JTextField();
        jtbAnoPreA185 = new javax.swing.JToggleButton();
        btnFotPreA185 = new javax.swing.JButton();
        jspAnoPreA185 = new javax.swing.JScrollPane();
        jtaAnoA185 = new javax.swing.JTextArea();
        txtPreA186 = new javax.swing.JTextField();
        jtbAnoPreA186 = new javax.swing.JToggleButton();
        btnFotPreA186 = new javax.swing.JButton();
        jspAnoPreA186 = new javax.swing.JScrollPane();
        jtaAnoA186 = new javax.swing.JTextArea();
        txtPreA187 = new javax.swing.JTextField();
        jtbAnoPreA187 = new javax.swing.JToggleButton();
        btnFotPreA187 = new javax.swing.JButton();
        jspAnoPreA187 = new javax.swing.JScrollPane();
        jtaAnoA187 = new javax.swing.JTextArea();
        txtPreA188 = new javax.swing.JTextField();
        jtbAnoPreA188 = new javax.swing.JToggleButton();
        btnFotPreA188 = new javax.swing.JButton();
        jspAnoPreA188 = new javax.swing.JScrollPane();
        jtaAnoA188 = new javax.swing.JTextArea();
        jtbAnoPreA189 = new javax.swing.JToggleButton();
        btnFotPreA189 = new javax.swing.JButton();
        txtPreA189 = new javax.swing.JTextField();
        jspAnoPreA189 = new javax.swing.JScrollPane();
        jtaAnoA189 = new javax.swing.JTextArea();
        jspAnoPreA190 = new javax.swing.JScrollPane();
        jtaAnoA190 = new javax.swing.JTextArea();
        txtPreA190 = new javax.swing.JTextField();
        jtbAnoPreA190 = new javax.swing.JToggleButton();
        btnFotPreA190 = new javax.swing.JButton();
        txtPreA191 = new javax.swing.JTextField();
        jtbAnoPreA191 = new javax.swing.JToggleButton();
        btnFotPreA191 = new javax.swing.JButton();
        jspAnoPreA191 = new javax.swing.JScrollPane();
        jtaAnoA191 = new javax.swing.JTextArea();
        txtPreA192 = new javax.swing.JTextField();
        jtbAnoPreA192 = new javax.swing.JToggleButton();
        btnFotPreA192 = new javax.swing.JButton();
        jspAnoPreA192 = new javax.swing.JScrollPane();
        jtaAnoA192 = new javax.swing.JTextArea();
        txtPreA193 = new javax.swing.JTextField();
        jtbAnoPreA193 = new javax.swing.JToggleButton();
        btnFotPreA193 = new javax.swing.JButton();
        jspAnoPreA193 = new javax.swing.JScrollPane();
        jtaAnoA193 = new javax.swing.JTextArea();
        txtPreA194 = new javax.swing.JTextField();
        jtbAnoPreA194 = new javax.swing.JToggleButton();
        btnFotPreA194 = new javax.swing.JButton();
        jspAnoPreA194 = new javax.swing.JScrollPane();
        jtaAnoA194 = new javax.swing.JTextArea();
        txtPreA195 = new javax.swing.JTextField();
        jtbAnoPreA195 = new javax.swing.JToggleButton();
        btnFotPreA195 = new javax.swing.JButton();
        jspAnoPreA195 = new javax.swing.JScrollPane();
        jtaAnoA195 = new javax.swing.JTextArea();
        txtPreA196 = new javax.swing.JTextField();
        jtbAnoPreA196 = new javax.swing.JToggleButton();
        btnFotPreA196 = new javax.swing.JButton();
        jspAnoPreA196 = new javax.swing.JScrollPane();
        jtaAnoA196 = new javax.swing.JTextArea();
        txtPreA197 = new javax.swing.JTextField();
        jspAnoPreA197 = new javax.swing.JScrollPane();
        jtaAnoA197 = new javax.swing.JTextArea();
        jtbAnoPreA197 = new javax.swing.JToggleButton();
        btnFotPreA197 = new javax.swing.JButton();
        txtPreA198 = new javax.swing.JTextField();
        jtbAnoPreA198 = new javax.swing.JToggleButton();
        btnFotPreA198 = new javax.swing.JButton();
        jspAnoPreA198 = new javax.swing.JScrollPane();
        jtaAnoA198 = new javax.swing.JTextArea();
        txtPreA199 = new javax.swing.JTextField();
        jtbAnoPreA199 = new javax.swing.JToggleButton();
        btnFotPreA199 = new javax.swing.JButton();
        jspAnoPreA199 = new javax.swing.JScrollPane();
        jtaAnoA199 = new javax.swing.JTextArea();
        txtPreA200 = new javax.swing.JTextField();
        jtbAnoPreA200 = new javax.swing.JToggleButton();
        btnFotPreA200 = new javax.swing.JButton();
        jspAnoPreA200 = new javax.swing.JScrollPane();
        jtaAnoA200 = new javax.swing.JTextArea();
        txtPreA201 = new javax.swing.JTextField();
        jtbAnoPreA201 = new javax.swing.JToggleButton();
        btnFotPreA201 = new javax.swing.JButton();
        jspAnoPreA201 = new javax.swing.JScrollPane();
        jtaAnoA201 = new javax.swing.JTextArea();
        txtPreA202 = new javax.swing.JTextField();
        jtbAnoPreA202 = new javax.swing.JToggleButton();
        btnFotPreA202 = new javax.swing.JButton();
        jspAnoPreA202 = new javax.swing.JScrollPane();
        jtaAnoA202 = new javax.swing.JTextArea();
        btnImpPre5 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jpaAba5 = new javax.swing.JPanel();
        txtIdPre6 = new javax.swing.JTextField();
        txtDatIni6 = new javax.swing.JTextField();
        txtDatFin6 = new javax.swing.JTextField();
        txtTemPre6 = new javax.swing.JTextField();
        txtDatPre6 = new javax.swing.JTextField();
        txtNomTec6 = new javax.swing.JTextField();
        jScrollPane18 = new javax.swing.JScrollPane();
        jtaEquPre6 = new javax.swing.JTextArea();
        jScrollPane19 = new javax.swing.JScrollPane();
        jtaSetPre6 = new javax.swing.JTextArea();
        txtCodEqu6 = new javax.swing.JTextField();
        txtStaPre6 = new javax.swing.JTextField();
        jtbAnoPreA203 = new javax.swing.JToggleButton();
        jspAnoPreA203 = new javax.swing.JScrollPane();
        jtaAnoA203 = new javax.swing.JTextArea();
        btnFotPreA203 = new javax.swing.JButton();
        txtPreA203 = new javax.swing.JTextField();
        jspAnoPreA204 = new javax.swing.JScrollPane();
        jtaAnoA204 = new javax.swing.JTextArea();
        txtPreA204 = new javax.swing.JTextField();
        jtbAnoPreA204 = new javax.swing.JToggleButton();
        btnFotPreA204 = new javax.swing.JButton();
        txtPreA205 = new javax.swing.JTextField();
        jtbAnoPreA205 = new javax.swing.JToggleButton();
        btnFotPreA205 = new javax.swing.JButton();
        jspAnoPreA205 = new javax.swing.JScrollPane();
        jtaAnoA205 = new javax.swing.JTextArea();
        txtPreA206 = new javax.swing.JTextField();
        jtbAnoPreA206 = new javax.swing.JToggleButton();
        btnFotPreA206 = new javax.swing.JButton();
        jspAnoPreA206 = new javax.swing.JScrollPane();
        jtaAnoA206 = new javax.swing.JTextArea();
        txtPreA207 = new javax.swing.JTextField();
        jtbAnoPreA207 = new javax.swing.JToggleButton();
        btnFotPreA207 = new javax.swing.JButton();
        jspAnoPreA207 = new javax.swing.JScrollPane();
        jtaAnoA207 = new javax.swing.JTextArea();
        txtPreA208 = new javax.swing.JTextField();
        jtbAnoPreA208 = new javax.swing.JToggleButton();
        btnFotPreA208 = new javax.swing.JButton();
        jspAnoPreA208 = new javax.swing.JScrollPane();
        jtaAnoA208 = new javax.swing.JTextArea();
        txtPreA209 = new javax.swing.JTextField();
        jtbAnoPreA209 = new javax.swing.JToggleButton();
        btnFotPreA209 = new javax.swing.JButton();
        jspAnoPreA209 = new javax.swing.JScrollPane();
        jtaAnoA209 = new javax.swing.JTextArea();
        txtPreA210 = new javax.swing.JTextField();
        jtbAnoPreA210 = new javax.swing.JToggleButton();
        btnFotPreA210 = new javax.swing.JButton();
        jspAnoPreA210 = new javax.swing.JScrollPane();
        jtaAnoA210 = new javax.swing.JTextArea();
        txtPreA211 = new javax.swing.JTextField();
        jspAnoPreA211 = new javax.swing.JScrollPane();
        jtaAnoA211 = new javax.swing.JTextArea();
        jtbAnoPreA211 = new javax.swing.JToggleButton();
        btnFotPreA211 = new javax.swing.JButton();
        txtPreA212 = new javax.swing.JTextField();
        jtbAnoPreA212 = new javax.swing.JToggleButton();
        btnFotPreA212 = new javax.swing.JButton();
        jspAnoPreA212 = new javax.swing.JScrollPane();
        jtaAnoA212 = new javax.swing.JTextArea();
        txtPreA213 = new javax.swing.JTextField();
        jtbAnoPreA213 = new javax.swing.JToggleButton();
        btnFotPreA213 = new javax.swing.JButton();
        jspAnoPreA213 = new javax.swing.JScrollPane();
        jtaAnoA213 = new javax.swing.JTextArea();
        txtPreA214 = new javax.swing.JTextField();
        jtbAnoPreA214 = new javax.swing.JToggleButton();
        btnFotPreA214 = new javax.swing.JButton();
        jspAnoPreA214 = new javax.swing.JScrollPane();
        jtaAnoA214 = new javax.swing.JTextArea();
        txtPreA215 = new javax.swing.JTextField();
        jtbAnoPreA215 = new javax.swing.JToggleButton();
        btnFotPreA215 = new javax.swing.JButton();
        jspAnoPreA215 = new javax.swing.JScrollPane();
        jtaAnoA215 = new javax.swing.JTextArea();
        txtPreA216 = new javax.swing.JTextField();
        jtbAnoPreA216 = new javax.swing.JToggleButton();
        btnFotPreA216 = new javax.swing.JButton();
        jspAnoPreA216 = new javax.swing.JScrollPane();
        jtaAnoA216 = new javax.swing.JTextArea();
        txtPreA217 = new javax.swing.JTextField();
        jtbAnoPreA217 = new javax.swing.JToggleButton();
        btnFotPreA217 = new javax.swing.JButton();
        jspAnoPreA217 = new javax.swing.JScrollPane();
        jtaAnoA217 = new javax.swing.JTextArea();
        txtPreA218 = new javax.swing.JTextField();
        jtbAnoPreA218 = new javax.swing.JToggleButton();
        btnFotPreA218 = new javax.swing.JButton();
        jspAnoPreA218 = new javax.swing.JScrollPane();
        jtaAnoA218 = new javax.swing.JTextArea();
        jtbAnoPreA219 = new javax.swing.JToggleButton();
        btnFotPreA219 = new javax.swing.JButton();
        jspAnoPreA219 = new javax.swing.JScrollPane();
        jtaAnoA219 = new javax.swing.JTextArea();
        txtPreA219 = new javax.swing.JTextField();
        jspAnoPreA220 = new javax.swing.JScrollPane();
        jtaAnoA220 = new javax.swing.JTextArea();
        txtPreA220 = new javax.swing.JTextField();
        jtbAnoPreA220 = new javax.swing.JToggleButton();
        btnFotPreA220 = new javax.swing.JButton();
        txtPreA221 = new javax.swing.JTextField();
        jtbAnoPreA221 = new javax.swing.JToggleButton();
        btnFotPreA221 = new javax.swing.JButton();
        jspAnoPreA221 = new javax.swing.JScrollPane();
        jtaAnoA221 = new javax.swing.JTextArea();
        txtPreA222 = new javax.swing.JTextField();
        jtbAnoPreA222 = new javax.swing.JToggleButton();
        btnFotPreA222 = new javax.swing.JButton();
        jspAnoPreA222 = new javax.swing.JScrollPane();
        jtaAnoA222 = new javax.swing.JTextArea();
        txtPreA223 = new javax.swing.JTextField();
        jtbAnoPreA223 = new javax.swing.JToggleButton();
        btnFotPreA223 = new javax.swing.JButton();
        jspAnoPreA223 = new javax.swing.JScrollPane();
        jtaAnoA223 = new javax.swing.JTextArea();
        txtPreA224 = new javax.swing.JTextField();
        jtbAnoPreA224 = new javax.swing.JToggleButton();
        btnFotPreA224 = new javax.swing.JButton();
        jspAnoPreA224 = new javax.swing.JScrollPane();
        jtaAnoA224 = new javax.swing.JTextArea();
        txtPreA225 = new javax.swing.JTextField();
        jtbAnoPreA225 = new javax.swing.JToggleButton();
        btnFotPreA225 = new javax.swing.JButton();
        jspAnoPreA225 = new javax.swing.JScrollPane();
        jtaAnoA225 = new javax.swing.JTextArea();
        txtPreA226 = new javax.swing.JTextField();
        jtbAnoPreA226 = new javax.swing.JToggleButton();
        btnFotPreA226 = new javax.swing.JButton();
        jspAnoPreA226 = new javax.swing.JScrollPane();
        jtaAnoA226 = new javax.swing.JTextArea();
        txtPreA227 = new javax.swing.JTextField();
        jspAnoPreA227 = new javax.swing.JScrollPane();
        jtaAnoA227 = new javax.swing.JTextArea();
        jtbAnoPreA227 = new javax.swing.JToggleButton();
        btnFotPreA227 = new javax.swing.JButton();
        txtPreA228 = new javax.swing.JTextField();
        jtbAnoPreA228 = new javax.swing.JToggleButton();
        btnFotPreA228 = new javax.swing.JButton();
        jspAnoPreA228 = new javax.swing.JScrollPane();
        jtaAnoA228 = new javax.swing.JTextArea();
        txtPreA229 = new javax.swing.JTextField();
        jtbAnoPreA229 = new javax.swing.JToggleButton();
        btnFotPreA229 = new javax.swing.JButton();
        jspAnoPreA229 = new javax.swing.JScrollPane();
        jtaAnoA229 = new javax.swing.JTextArea();
        txtPreA230 = new javax.swing.JTextField();
        jtbAnoPreA230 = new javax.swing.JToggleButton();
        btnFotPreA230 = new javax.swing.JButton();
        jspAnoPreA230 = new javax.swing.JScrollPane();
        jtaAnoA230 = new javax.swing.JTextArea();
        txtPreA231 = new javax.swing.JTextField();
        jtbAnoPreA231 = new javax.swing.JToggleButton();
        btnFotPreA231 = new javax.swing.JButton();
        jspAnoPreA231 = new javax.swing.JScrollPane();
        jtaAnoA231 = new javax.swing.JTextArea();
        txtPreA232 = new javax.swing.JTextField();
        jtbAnoPreA232 = new javax.swing.JToggleButton();
        btnFotPreA232 = new javax.swing.JButton();
        jspAnoPreA232 = new javax.swing.JScrollPane();
        jtaAnoA232 = new javax.swing.JTextArea();
        txtPreA233 = new javax.swing.JTextField();
        jtbAnoPreA233 = new javax.swing.JToggleButton();
        btnFotPreA233 = new javax.swing.JButton();
        jspAnoPreA233 = new javax.swing.JScrollPane();
        jtaAnoA233 = new javax.swing.JTextArea();
        txtPreA234 = new javax.swing.JTextField();
        jtbAnoPreA234 = new javax.swing.JToggleButton();
        btnFotPreA234 = new javax.swing.JButton();
        jspAnoPreA234 = new javax.swing.JScrollPane();
        jtaAnoA234 = new javax.swing.JTextArea();
        jtbAnoPreA235 = new javax.swing.JToggleButton();
        btnFotPreA235 = new javax.swing.JButton();
        txtPreA235 = new javax.swing.JTextField();
        jspAnoPreA235 = new javax.swing.JScrollPane();
        jtaAnoA235 = new javax.swing.JTextArea();
        btnImpPre4 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        jpaArqPre.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        jpaArqPre.setLayout(null);

        tblArqPre =  new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblArqPre.setAutoCreateRowSorter(true);
        tblArqPre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "N° Preventiva", "Nome do Arquivo"
            }
        ));
        tblArqPre.getTableHeader().setReorderingAllowed(false);
        tblArqPre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblArqPreMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblArqPre);

        jpaArqPre.add(jScrollPane5);
        jScrollPane5.setBounds(15, 270, 240, 76);

        lblFotEltMen8.setBackground(new java.awt.Color(153, 153, 153));
        lblFotEltMen8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        lblFotEltMen8.setOpaque(true);
        jpaArqPre.add(lblFotEltMen8);
        lblFotEltMen8.setBounds(15, 15, 240, 180);

        txtFotPre8.setEditable(false);
        txtFotPre8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtFotPre8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaArqPre.add(txtFotPre8);
        txtFotPre8.setBounds(15, 206, 93, 26);

        btnSalFotPre8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSalFotPre8.setText("Salvar");
        btnSalFotPre8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnSalFotPre8.setEnabled(false);
        btnSalFotPre8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalFotPre8ActionPerformed(evt);
            }
        });
        jpaArqPre.add(btnSalFotPre8);
        btnSalFotPre8.setBounds(110, 206, 73, 26);

        txtCanFot.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCanFot.setText("Cancelar");
        txtCanFot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        txtCanFot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanFotActionPerformed(evt);
            }
        });
        jpaArqPre.add(txtCanFot);
        txtCanFot.setBounds(183, 206, 73, 26);

        txtPesArq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesArq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesArqKeyReleased(evt);
            }
        });
        jpaArqPre.add(txtPesArq);
        txtPesArq.setBounds(80, 238, 93, 26);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 66));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("N°");
        jpaArqPre.add(jLabel1);
        jLabel1.setBounds(176, 242, 20, 20);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 66));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Psquisar");
        jpaArqPre.add(jLabel2);
        jLabel2.setBounds(11, 236, 70, 26);

        txtQanArq.setEditable(false);
        txtQanArq.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtQanArq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaArqPre.add(txtQanArq);
        txtQanArq.setBounds(194, 238, 60, 26);

        getContentPane().add(jpaArqPre);
        jpaArqPre.setBounds(390, 90, 270, 20);

        jtpAbas1.setName(""); // NOI18N
        jtpAbas1.setPreferredSize(new java.awt.Dimension(967, 632));

        jScrollPane14.setPreferredSize(new java.awt.Dimension(962, 2025));

        jpaAba4.setPreferredSize(new java.awt.Dimension(960, 2025));
        jpaAba4.setLayout(null);

        txtIdPre3.setEditable(false);
        txtIdPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtIdPre3);
        txtIdPre3.setBounds(58, 182, 100, 28);

        txtDatIni3.setEditable(false);
        txtDatIni3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtDatIni3);
        txtDatIni3.setBounds(188, 182, 170, 28);

        txtDatFin3.setEditable(false);
        txtDatFin3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtDatFin3);
        txtDatFin3.setBounds(388, 182, 170, 28);

        txtTemPre3.setEditable(false);
        txtTemPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtTemPre3);
        txtTemPre3.setBounds(588, 182, 170, 28);

        txtDatPre3.setEditable(false);
        txtDatPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtDatPre3);
        txtDatPre3.setBounds(788, 182, 140, 28);

        txtNomTec3.setEditable(false);
        txtNomTec3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtNomTec3);
        txtNomTec3.setBounds(58, 243, 180, 28);

        jScrollPane15.setEnabled(false);

        jtaEquPre3.setEditable(false);
        jtaEquPre3.setColumns(20);
        jtaEquPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre3.setLineWrap(true);
        jtaEquPre3.setRows(5);
        jScrollPane15.setViewportView(jtaEquPre3);

        jpaAba4.add(jScrollPane15);
        jScrollPane15.setBounds(477, 238, 184, 38);

        jScrollPane16.setEnabled(false);

        jtaSetPre3.setEditable(false);
        jtaSetPre3.setColumns(20);
        jtaSetPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre3.setLineWrap(true);
        jtaSetPre3.setRows(5);
        jScrollPane16.setViewportView(jtaSetPre3);

        jpaAba4.add(jScrollPane16);
        jScrollPane16.setBounds(266, 238, 184, 38);

        txtCodEqu3.setEditable(false);
        txtCodEqu3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtCodEqu3);
        txtCodEqu3.setBounds(688, 243, 100, 28);

        txtStaPre3.setEditable(false);
        txtStaPre3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtStaPre3);
        txtStaPre3.setBounds(818, 243, 110, 28);

        jtbAnoPreA119.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA119.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA119ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA119);
        jtbAnoPreA119.setBounds(824, 391, 40, 40);

        btnFotPreA119.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA119.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA119ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA119);
        btnFotPreA119.setBounds(880, 391, 40, 40);

        txtPreA119.setEditable(false);
        txtPreA119.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA119.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA119);
        txtPreA119.setBounds(704, 397, 109, 28);

        jtaAnoA119.setEditable(false);
        jtaAnoA119.setColumns(20);
        jtaAnoA119.setLineWrap(true);
        jtaAnoA119.setRows(5);
        jspAnoPreA119.setViewportView(jtaAnoA119);

        jpaAba4.add(jspAnoPreA119);
        jspAnoPreA119.setBounds(750, 428, 40, 10);

        jtaAnoA120.setEditable(false);
        jtaAnoA120.setColumns(20);
        jtaAnoA120.setLineWrap(true);
        jtaAnoA120.setRows(5);
        jspAnoPreA120.setViewportView(jtaAnoA120);

        jpaAba4.add(jspAnoPreA120);
        jspAnoPreA120.setBounds(750, 481, 40, 10);

        txtPreA120.setEditable(false);
        txtPreA120.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA120.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA120);
        txtPreA120.setBounds(704, 450, 109, 28);

        jtbAnoPreA120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA120.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA120ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA120);
        jtbAnoPreA120.setBounds(824, 444, 40, 40);

        btnFotPreA120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA120.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA120ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA120);
        btnFotPreA120.setBounds(880, 444, 40, 40);

        txtPreA121.setEditable(false);
        txtPreA121.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA121.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA121);
        txtPreA121.setBounds(704, 504, 109, 28);

        jtbAnoPreA121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA121ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA121);
        jtbAnoPreA121.setBounds(824, 498, 40, 40);

        btnFotPreA121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA121ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA121);
        btnFotPreA121.setBounds(880, 498, 40, 40);

        jtaAnoA121.setEditable(false);
        jtaAnoA121.setColumns(20);
        jtaAnoA121.setLineWrap(true);
        jtaAnoA121.setRows(5);
        jspAnoPreA121.setViewportView(jtaAnoA121);

        jpaAba4.add(jspAnoPreA121);
        jspAnoPreA121.setBounds(750, 535, 40, 10);

        txtPreA122.setEditable(false);
        txtPreA122.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA122.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA122);
        txtPreA122.setBounds(704, 556, 109, 28);

        jtbAnoPreA122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA122ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA122);
        jtbAnoPreA122.setBounds(824, 550, 40, 40);

        btnFotPreA122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA122ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA122);
        btnFotPreA122.setBounds(880, 550, 40, 40);

        jtaAnoA122.setEditable(false);
        jtaAnoA122.setColumns(20);
        jtaAnoA122.setLineWrap(true);
        jtaAnoA122.setRows(5);
        jspAnoPreA122.setViewportView(jtaAnoA122);

        jpaAba4.add(jspAnoPreA122);
        jspAnoPreA122.setBounds(750, 587, 40, 10);

        txtPreA123.setEditable(false);
        txtPreA123.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA123.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA123);
        txtPreA123.setBounds(704, 610, 109, 28);

        jtbAnoPreA123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA123ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA123);
        jtbAnoPreA123.setBounds(824, 603, 40, 40);

        btnFotPreA123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA123ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA123);
        btnFotPreA123.setBounds(880, 603, 40, 40);

        jtaAnoA123.setEditable(false);
        jtaAnoA123.setColumns(20);
        jtaAnoA123.setLineWrap(true);
        jtaAnoA123.setRows(5);
        jspAnoPreA123.setViewportView(jtaAnoA123);

        jpaAba4.add(jspAnoPreA123);
        jspAnoPreA123.setBounds(750, 640, 40, 10);

        txtPreA124.setEditable(false);
        txtPreA124.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA124.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA124);
        txtPreA124.setBounds(704, 660, 109, 28);

        jtbAnoPreA124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA124ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA124);
        jtbAnoPreA124.setBounds(824, 656, 40, 40);

        btnFotPreA124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA124ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA124);
        btnFotPreA124.setBounds(880, 656, 40, 40);

        jtaAnoA124.setEditable(false);
        jtaAnoA124.setColumns(20);
        jtaAnoA124.setLineWrap(true);
        jtaAnoA124.setRows(5);
        jspAnoPreA124.setViewportView(jtaAnoA124);

        jpaAba4.add(jspAnoPreA124);
        jspAnoPreA124.setBounds(750, 693, 40, 10);

        txtPreA125.setEditable(false);
        txtPreA125.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA125.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA125);
        txtPreA125.setBounds(704, 716, 109, 28);

        jtbAnoPreA125.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA125ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA125);
        jtbAnoPreA125.setBounds(824, 709, 40, 40);

        btnFotPreA125.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA125ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA125);
        btnFotPreA125.setBounds(880, 709, 40, 40);

        jtaAnoA125.setEditable(false);
        jtaAnoA125.setColumns(20);
        jtaAnoA125.setLineWrap(true);
        jtaAnoA125.setRows(5);
        jspAnoPreA125.setViewportView(jtaAnoA125);

        jpaAba4.add(jspAnoPreA125);
        jspAnoPreA125.setBounds(750, 746, 40, 10);

        txtPreA126.setEditable(false);
        txtPreA126.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA126.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA126);
        txtPreA126.setBounds(704, 770, 109, 28);

        jtbAnoPreA126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA126.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA126ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA126);
        jtbAnoPreA126.setBounds(824, 763, 40, 40);

        btnFotPreA126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA126.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA126ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA126);
        btnFotPreA126.setBounds(880, 763, 40, 40);

        jtaAnoA126.setEditable(false);
        jtaAnoA126.setColumns(20);
        jtaAnoA126.setLineWrap(true);
        jtaAnoA126.setRows(5);
        jspAnoPreA126.setViewportView(jtaAnoA126);

        jpaAba4.add(jspAnoPreA126);
        jspAnoPreA126.setBounds(750, 800, 40, 10);

        txtPreA127.setEditable(false);
        txtPreA127.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA127.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA127);
        txtPreA127.setBounds(704, 822, 109, 28);

        jtaAnoA127.setEditable(false);
        jtaAnoA127.setColumns(20);
        jtaAnoA127.setLineWrap(true);
        jtaAnoA127.setRows(5);
        jspAnoPreA127.setViewportView(jtaAnoA127);

        jpaAba4.add(jspAnoPreA127);
        jspAnoPreA127.setBounds(750, 853, 40, 10);

        jtbAnoPreA127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA127ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA127);
        jtbAnoPreA127.setBounds(824, 816, 40, 40);

        btnFotPreA127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA127ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA127);
        btnFotPreA127.setBounds(880, 816, 40, 40);

        txtPreA128.setEditable(false);
        txtPreA128.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA128.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA128);
        txtPreA128.setBounds(704, 875, 109, 28);

        jtbAnoPreA128.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA128ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA128);
        jtbAnoPreA128.setBounds(824, 868, 40, 40);

        btnFotPreA128.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA128ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA128);
        btnFotPreA128.setBounds(880, 868, 40, 40);

        jtaAnoA128.setEditable(false);
        jtaAnoA128.setColumns(20);
        jtaAnoA128.setLineWrap(true);
        jtaAnoA128.setRows(5);
        jspAnoPreA128.setViewportView(jtaAnoA128);

        jpaAba4.add(jspAnoPreA128);
        jspAnoPreA128.setBounds(750, 906, 40, 10);

        txtPreA129.setEditable(false);
        txtPreA129.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA129.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA129);
        txtPreA129.setBounds(704, 928, 109, 28);

        jtbAnoPreA129.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA129.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA129ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA129);
        jtbAnoPreA129.setBounds(824, 922, 40, 40);

        btnFotPreA129.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA129.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA129ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA129);
        btnFotPreA129.setBounds(880, 922, 40, 40);

        jtaAnoA129.setEditable(false);
        jtaAnoA129.setColumns(20);
        jtaAnoA129.setLineWrap(true);
        jtaAnoA129.setRows(5);
        jspAnoPreA129.setViewportView(jtaAnoA129);

        jpaAba4.add(jspAnoPreA129);
        jspAnoPreA129.setBounds(750, 959, 40, 10);

        txtPreA130.setEditable(false);
        txtPreA130.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA130.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA130);
        txtPreA130.setBounds(704, 980, 109, 28);

        jtbAnoPreA130.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA130.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA130ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA130);
        jtbAnoPreA130.setBounds(824, 974, 40, 40);

        btnFotPreA130.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA130.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA130ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA130);
        btnFotPreA130.setBounds(880, 974, 40, 40);

        jtaAnoA130.setEditable(false);
        jtaAnoA130.setColumns(20);
        jtaAnoA130.setLineWrap(true);
        jtaAnoA130.setRows(5);
        jspAnoPreA130.setViewportView(jtaAnoA130);

        jpaAba4.add(jspAnoPreA130);
        jspAnoPreA130.setBounds(750, 1012, 40, 10);

        txtPreA131.setEditable(false);
        txtPreA131.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA131.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA131);
        txtPreA131.setBounds(704, 1034, 109, 28);

        jtbAnoPreA131.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA131.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA131ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA131);
        jtbAnoPreA131.setBounds(824, 1028, 40, 40);

        btnFotPreA131.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA131.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA131ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA131);
        btnFotPreA131.setBounds(880, 1028, 40, 40);

        jtaAnoA131.setEditable(false);
        jtaAnoA131.setColumns(20);
        jtaAnoA131.setLineWrap(true);
        jtaAnoA131.setRows(5);
        jspAnoPreA131.setViewportView(jtaAnoA131);

        jpaAba4.add(jspAnoPreA131);
        jspAnoPreA131.setBounds(750, 1065, 40, 10);

        txtPreA132.setEditable(false);
        txtPreA132.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA132.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA132);
        txtPreA132.setBounds(704, 1086, 109, 28);

        jtbAnoPreA132.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA132.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA132ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA132);
        jtbAnoPreA132.setBounds(824, 1080, 40, 40);

        btnFotPreA132.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA132.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA132ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA132);
        btnFotPreA132.setBounds(880, 1080, 40, 40);

        jtaAnoA132.setEditable(false);
        jtaAnoA132.setColumns(20);
        jtaAnoA132.setLineWrap(true);
        jtaAnoA132.setRows(5);
        jspAnoPreA132.setViewportView(jtaAnoA132);

        jpaAba4.add(jspAnoPreA132);
        jspAnoPreA132.setBounds(750, 1118, 40, 10);

        txtPreA133.setEditable(false);
        txtPreA133.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA133.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA133);
        txtPreA133.setBounds(704, 1140, 109, 28);

        jtbAnoPreA133.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA133.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA133ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA133);
        jtbAnoPreA133.setBounds(824, 1133, 40, 40);

        btnFotPreA133.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA133.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA133ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA133);
        btnFotPreA133.setBounds(880, 1133, 40, 40);

        jtaAnoA133.setEditable(false);
        jtaAnoA133.setColumns(20);
        jtaAnoA133.setLineWrap(true);
        jtaAnoA133.setRows(5);
        jspAnoPreA133.setViewportView(jtaAnoA133);

        jpaAba4.add(jspAnoPreA133);
        jspAnoPreA133.setBounds(750, 1171, 40, 10);

        txtPreA134.setEditable(false);
        txtPreA134.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA134.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA134);
        txtPreA134.setBounds(704, 1192, 109, 28);

        jtbAnoPreA134.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA134.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA134ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA134);
        jtbAnoPreA134.setBounds(824, 1186, 40, 40);

        btnFotPreA134.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA134.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA134ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA134);
        btnFotPreA134.setBounds(880, 1186, 40, 40);

        jtaAnoA134.setEditable(false);
        jtaAnoA134.setColumns(20);
        jtaAnoA134.setLineWrap(true);
        jtaAnoA134.setRows(5);
        jspAnoPreA134.setViewportView(jtaAnoA134);

        jpaAba4.add(jspAnoPreA134);
        jspAnoPreA134.setBounds(750, 1223, 40, 10);

        jtaAnoA135.setEditable(false);
        jtaAnoA135.setColumns(20);
        jtaAnoA135.setLineWrap(true);
        jtaAnoA135.setRows(5);
        jspAnoPreA135.setViewportView(jtaAnoA135);

        jpaAba4.add(jspAnoPreA135);
        jspAnoPreA135.setBounds(750, 1276, 40, 10);

        jtaAnoA136.setEditable(false);
        jtaAnoA136.setColumns(20);
        jtaAnoA136.setLineWrap(true);
        jtaAnoA136.setRows(5);
        jspAnoPreA136.setViewportView(jtaAnoA136);

        jpaAba4.add(jspAnoPreA136);
        jspAnoPreA136.setBounds(750, 1329, 40, 10);

        jtbAnoPreA135.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA135.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA135ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA135);
        jtbAnoPreA135.setBounds(824, 1240, 40, 40);

        btnFotPreA135.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA135.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA135ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA135);
        btnFotPreA135.setBounds(880, 1240, 40, 40);

        txtPreA135.setEditable(false);
        txtPreA135.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA135.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA135);
        txtPreA135.setBounds(704, 1246, 109, 28);

        txtPreA136.setEditable(false);
        txtPreA136.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA136.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA136);
        txtPreA136.setBounds(704, 1298, 109, 28);

        jtbAnoPreA136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA136.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA136ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA136);
        jtbAnoPreA136.setBounds(824, 1292, 40, 40);

        btnFotPreA136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA136.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA136ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA136);
        btnFotPreA136.setBounds(880, 1292, 40, 40);

        txtPreA137.setEditable(false);
        txtPreA137.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA137.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA137);
        txtPreA137.setBounds(704, 1352, 109, 28);

        jtbAnoPreA137.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA137.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA137ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA137);
        jtbAnoPreA137.setBounds(824, 1345, 40, 40);

        btnFotPreA137.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA137.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA137ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA137);
        btnFotPreA137.setBounds(880, 1345, 40, 40);

        jtaAnoA137.setEditable(false);
        jtaAnoA137.setColumns(20);
        jtaAnoA137.setLineWrap(true);
        jtaAnoA137.setRows(5);
        jspAnoPreA137.setViewportView(jtaAnoA137);

        jpaAba4.add(jspAnoPreA137);
        jspAnoPreA137.setBounds(750, 1383, 40, 10);

        txtPreA138.setEditable(false);
        txtPreA138.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA138.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA138);
        txtPreA138.setBounds(704, 1406, 109, 28);

        jtbAnoPreA138.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA138.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA138ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA138);
        jtbAnoPreA138.setBounds(824, 1398, 40, 40);

        btnFotPreA138.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA138.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA138ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA138);
        btnFotPreA138.setBounds(880, 1398, 40, 40);

        jtaAnoA138.setEditable(false);
        jtaAnoA138.setColumns(20);
        jtaAnoA138.setLineWrap(true);
        jtaAnoA138.setRows(5);
        jspAnoPreA138.setViewportView(jtaAnoA138);

        jpaAba4.add(jspAnoPreA138);
        jspAnoPreA138.setBounds(750, 1436, 40, 10);

        txtPreA139.setEditable(false);
        txtPreA139.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA139.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA139);
        txtPreA139.setBounds(704, 1458, 109, 28);

        jtbAnoPreA139.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA139.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA139ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA139);
        jtbAnoPreA139.setBounds(824, 1452, 40, 40);

        btnFotPreA139.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA139.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA139ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA139);
        btnFotPreA139.setBounds(880, 1452, 40, 40);

        jtaAnoA139.setEditable(false);
        jtaAnoA139.setColumns(20);
        jtaAnoA139.setLineWrap(true);
        jtaAnoA139.setRows(5);
        jspAnoPreA139.setViewportView(jtaAnoA139);

        jpaAba4.add(jspAnoPreA139);
        jspAnoPreA139.setBounds(750, 1489, 40, 10);

        txtPreA140.setEditable(false);
        txtPreA140.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA140.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA140);
        txtPreA140.setBounds(704, 1510, 109, 28);

        jtbAnoPreA140.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA140.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA140ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA140);
        jtbAnoPreA140.setBounds(824, 1505, 40, 40);

        btnFotPreA140.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA140.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA140ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA140);
        btnFotPreA140.setBounds(880, 1505, 40, 40);

        jtaAnoA140.setEditable(false);
        jtaAnoA140.setColumns(20);
        jtaAnoA140.setLineWrap(true);
        jtaAnoA140.setRows(5);
        jspAnoPreA140.setViewportView(jtaAnoA140);

        jpaAba4.add(jspAnoPreA140);
        jspAnoPreA140.setBounds(750, 1542, 40, 10);

        txtPreA141.setEditable(false);
        txtPreA141.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA141.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA141);
        txtPreA141.setBounds(704, 1564, 109, 28);

        jtbAnoPreA141.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA141.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA141ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA141);
        jtbAnoPreA141.setBounds(824, 1557, 40, 40);

        btnFotPreA141.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA141.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA141ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA141);
        btnFotPreA141.setBounds(880, 1557, 40, 40);

        jtaAnoA141.setEditable(false);
        jtaAnoA141.setColumns(20);
        jtaAnoA141.setLineWrap(true);
        jtaAnoA141.setRows(5);
        jspAnoPreA141.setViewportView(jtaAnoA141);

        jpaAba4.add(jspAnoPreA141);
        jspAnoPreA141.setBounds(750, 1595, 40, 10);

        txtPreA142.setEditable(false);
        txtPreA142.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA142.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA142);
        txtPreA142.setBounds(704, 1616, 109, 28);

        jtbAnoPreA142.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA142.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA142ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA142);
        jtbAnoPreA142.setBounds(824, 1610, 40, 40);

        btnFotPreA142.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA142.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA142ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA142);
        btnFotPreA142.setBounds(880, 1610, 40, 40);

        jtaAnoA142.setEditable(false);
        jtaAnoA142.setColumns(20);
        jtaAnoA142.setLineWrap(true);
        jtaAnoA142.setRows(5);
        jspAnoPreA142.setViewportView(jtaAnoA142);

        jpaAba4.add(jspAnoPreA142);
        jspAnoPreA142.setBounds(750, 1648, 40, 10);

        txtPreA143.setEditable(false);
        txtPreA143.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA143.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA143);
        txtPreA143.setBounds(704, 1670, 109, 28);

        jtaAnoA143.setEditable(false);
        jtaAnoA143.setColumns(20);
        jtaAnoA143.setLineWrap(true);
        jtaAnoA143.setRows(5);
        jspAnoPreA143.setViewportView(jtaAnoA143);

        jpaAba4.add(jspAnoPreA143);
        jspAnoPreA143.setBounds(750, 1701, 40, 10);

        jtbAnoPreA143.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA143.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA143ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA143);
        jtbAnoPreA143.setBounds(824, 1663, 40, 40);

        btnFotPreA143.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA143.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA143ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA143);
        btnFotPreA143.setBounds(880, 1663, 40, 40);

        txtPreA144.setEditable(false);
        txtPreA144.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA144.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA144);
        txtPreA144.setBounds(704, 1723, 109, 28);

        jtbAnoPreA144.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA144.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA144ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA144);
        jtbAnoPreA144.setBounds(824, 1716, 40, 40);

        btnFotPreA144.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA144.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA144ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA144);
        btnFotPreA144.setBounds(880, 1716, 40, 40);

        jtaAnoA144.setEditable(false);
        jtaAnoA144.setColumns(20);
        jtaAnoA144.setLineWrap(true);
        jtaAnoA144.setRows(5);
        jspAnoPreA144.setViewportView(jtaAnoA144);

        jpaAba4.add(jspAnoPreA144);
        jspAnoPreA144.setBounds(750, 1754, 40, 10);

        txtPreA145.setEditable(false);
        txtPreA145.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA145.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba4.add(txtPreA145);
        txtPreA145.setBounds(704, 1776, 109, 28);

        jtbAnoPreA145.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA145.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA145ActionPerformed(evt);
            }
        });
        jpaAba4.add(jtbAnoPreA145);
        jtbAnoPreA145.setBounds(824, 1770, 40, 40);

        btnFotPreA145.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA145.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA145ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnFotPreA145);
        btnFotPreA145.setBounds(880, 1770, 40, 40);

        jtaAnoA145.setEditable(false);
        jtaAnoA145.setColumns(20);
        jtaAnoA145.setLineWrap(true);
        jtaAnoA145.setRows(5);
        jspAnoPreA145.setViewportView(jtaAnoA145);

        jpaAba4.add(jspAnoPreA145);
        jspAnoPreA145.setBounds(750, 1807, 40, 10);

        btnImpPre3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre3.setToolTipText("Imprimir Documento");
        btnImpPre3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPre3ActionPerformed(evt);
            }
        });
        jpaAba4.add(btnImpPre3);
        btnImpPre3.setBounds(822, 1923, 64, 64);

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Hidrau mensal1 – 1.png"))); // NOI18N
        jpaAba4.add(jLabel21);
        jLabel21.setBounds(-12, 0, 1000, 2025);

        jScrollPane14.setViewportView(jpaAba4);

        jtpAbas1.addTab("Hidráulica Mensal", jScrollPane14);

        jpaAba6.setPreferredSize(new java.awt.Dimension(960, 2225));
        jpaAba6.setLayout(null);

        txtIdPre5.setEditable(false);
        txtIdPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtIdPre5);
        txtIdPre5.setBounds(58, 182, 100, 28);

        txtDatIni5.setEditable(false);
        txtDatIni5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtDatIni5);
        txtDatIni5.setBounds(188, 182, 170, 28);

        txtDatFin5.setEditable(false);
        txtDatFin5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtDatFin5);
        txtDatFin5.setBounds(388, 182, 170, 28);

        txtTemPre5.setEditable(false);
        txtTemPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtTemPre5);
        txtTemPre5.setBounds(588, 182, 170, 28);

        txtDatPre5.setEditable(false);
        txtDatPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtDatPre5);
        txtDatPre5.setBounds(788, 182, 140, 28);

        txtNomTec5.setEditable(false);
        txtNomTec5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtNomTec5);
        txtNomTec5.setBounds(58, 243, 180, 28);

        jScrollPane13.setEnabled(false);

        jtaEquPre5.setEditable(false);
        jtaEquPre5.setColumns(20);
        jtaEquPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre5.setLineWrap(true);
        jtaEquPre5.setRows(5);
        jScrollPane13.setViewportView(jtaEquPre5);

        jpaAba6.add(jScrollPane13);
        jScrollPane13.setBounds(477, 238, 184, 38);

        jScrollPane20.setEnabled(false);

        jtaSetPre5.setEditable(false);
        jtaSetPre5.setColumns(20);
        jtaSetPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre5.setLineWrap(true);
        jtaSetPre5.setRows(5);
        jScrollPane20.setViewportView(jtaSetPre5);

        jpaAba6.add(jScrollPane20);
        jScrollPane20.setBounds(266, 238, 184, 38);

        txtCodEqu5.setEditable(false);
        txtCodEqu5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtCodEqu5);
        txtCodEqu5.setBounds(688, 243, 100, 28);

        txtStaPre5.setEditable(false);
        txtStaPre5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtStaPre5);
        txtStaPre5.setBounds(818, 243, 110, 28);

        jtbAnoPreA173.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA173.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA173ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA173);
        jtbAnoPreA173.setBounds(824, 391, 40, 40);

        btnFotPreA173.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA173.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA173ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA173);
        btnFotPreA173.setBounds(880, 391, 40, 40);

        txtPreA173.setEditable(false);
        txtPreA173.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA173.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA173);
        txtPreA173.setBounds(704, 397, 109, 28);

        jtaAnoA173.setEditable(false);
        jtaAnoA173.setColumns(20);
        jtaAnoA173.setLineWrap(true);
        jtaAnoA173.setRows(5);
        jspAnoPreA173.setViewportView(jtaAnoA173);

        jpaAba6.add(jspAnoPreA173);
        jspAnoPreA173.setBounds(750, 428, 40, 10);

        jtaAnoA174.setEditable(false);
        jtaAnoA174.setColumns(20);
        jtaAnoA174.setLineWrap(true);
        jtaAnoA174.setRows(5);
        jspAnoPreA174.setViewportView(jtaAnoA174);

        jpaAba6.add(jspAnoPreA174);
        jspAnoPreA174.setBounds(750, 481, 40, 10);

        txtPreA174.setEditable(false);
        txtPreA174.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA174.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA174);
        txtPreA174.setBounds(704, 450, 109, 28);

        jtbAnoPreA174.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA174.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA174ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA174);
        jtbAnoPreA174.setBounds(824, 444, 40, 40);

        btnFotPreA174.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA174.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA174ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA174);
        btnFotPreA174.setBounds(880, 444, 40, 40);

        txtPreA175.setEditable(false);
        txtPreA175.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA175.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA175);
        txtPreA175.setBounds(704, 504, 109, 28);

        jtbAnoPreA175.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA175.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA175ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA175);
        jtbAnoPreA175.setBounds(824, 498, 40, 40);

        btnFotPreA175.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA175.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA175ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA175);
        btnFotPreA175.setBounds(880, 498, 40, 40);

        jtaAnoA175.setEditable(false);
        jtaAnoA175.setColumns(20);
        jtaAnoA175.setLineWrap(true);
        jtaAnoA175.setRows(5);
        jspAnoPreA175.setViewportView(jtaAnoA175);

        jpaAba6.add(jspAnoPreA175);
        jspAnoPreA175.setBounds(750, 535, 40, 10);

        txtPreA176.setEditable(false);
        txtPreA176.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA176.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA176);
        txtPreA176.setBounds(704, 556, 109, 28);

        jtbAnoPreA176.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA176.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA176ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA176);
        jtbAnoPreA176.setBounds(824, 550, 40, 40);

        btnFotPreA176.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA176.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA176ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA176);
        btnFotPreA176.setBounds(880, 550, 40, 40);

        jtaAnoA176.setEditable(false);
        jtaAnoA176.setColumns(20);
        jtaAnoA176.setLineWrap(true);
        jtaAnoA176.setRows(5);
        jspAnoPreA176.setViewportView(jtaAnoA176);

        jpaAba6.add(jspAnoPreA176);
        jspAnoPreA176.setBounds(750, 587, 40, 10);

        txtPreA177.setEditable(false);
        txtPreA177.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA177.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA177);
        txtPreA177.setBounds(704, 610, 109, 28);

        jtbAnoPreA177.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA177.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA177ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA177);
        jtbAnoPreA177.setBounds(824, 603, 40, 40);

        btnFotPreA177.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA177.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA177ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA177);
        btnFotPreA177.setBounds(880, 603, 40, 40);

        jtaAnoA177.setEditable(false);
        jtaAnoA177.setColumns(20);
        jtaAnoA177.setLineWrap(true);
        jtaAnoA177.setRows(5);
        jspAnoPreA177.setViewportView(jtaAnoA177);

        jpaAba6.add(jspAnoPreA177);
        jspAnoPreA177.setBounds(750, 640, 40, 10);

        txtPreA178.setEditable(false);
        txtPreA178.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA178.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA178);
        txtPreA178.setBounds(704, 660, 109, 28);

        jtbAnoPreA178.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA178.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA178ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA178);
        jtbAnoPreA178.setBounds(824, 656, 40, 40);

        btnFotPreA178.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA178.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA178ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA178);
        btnFotPreA178.setBounds(880, 656, 40, 40);

        jtaAnoA178.setEditable(false);
        jtaAnoA178.setColumns(20);
        jtaAnoA178.setLineWrap(true);
        jtaAnoA178.setRows(5);
        jspAnoPreA178.setViewportView(jtaAnoA178);

        jpaAba6.add(jspAnoPreA178);
        jspAnoPreA178.setBounds(750, 693, 40, 10);

        txtPreA179.setEditable(false);
        txtPreA179.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA179.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA179);
        txtPreA179.setBounds(704, 716, 109, 28);

        jtbAnoPreA179.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA179.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA179ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA179);
        jtbAnoPreA179.setBounds(824, 709, 40, 40);

        btnFotPreA179.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA179.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA179ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA179);
        btnFotPreA179.setBounds(880, 709, 40, 40);

        jtaAnoA179.setEditable(false);
        jtaAnoA179.setColumns(20);
        jtaAnoA179.setLineWrap(true);
        jtaAnoA179.setRows(5);
        jspAnoPreA179.setViewportView(jtaAnoA179);

        jpaAba6.add(jspAnoPreA179);
        jspAnoPreA179.setBounds(750, 746, 40, 10);

        txtPreA180.setEditable(false);
        txtPreA180.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA180.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA180);
        txtPreA180.setBounds(704, 770, 109, 28);

        jtbAnoPreA180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA180ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA180);
        jtbAnoPreA180.setBounds(824, 763, 40, 40);

        btnFotPreA180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA180ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA180);
        btnFotPreA180.setBounds(880, 763, 40, 40);

        jtaAnoA180.setEditable(false);
        jtaAnoA180.setColumns(20);
        jtaAnoA180.setLineWrap(true);
        jtaAnoA180.setRows(5);
        jspAnoPreA180.setViewportView(jtaAnoA180);

        jpaAba6.add(jspAnoPreA180);
        jspAnoPreA180.setBounds(750, 800, 40, 10);

        txtPreA181.setEditable(false);
        txtPreA181.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA181.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA181);
        txtPreA181.setBounds(704, 822, 109, 28);

        jtaAnoA181.setEditable(false);
        jtaAnoA181.setColumns(20);
        jtaAnoA181.setLineWrap(true);
        jtaAnoA181.setRows(5);
        jspAnoPreA181.setViewportView(jtaAnoA181);

        jpaAba6.add(jspAnoPreA181);
        jspAnoPreA181.setBounds(750, 853, 40, 10);

        jtbAnoPreA181.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA181.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA181ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA181);
        jtbAnoPreA181.setBounds(824, 816, 40, 40);

        btnFotPreA181.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA181.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA181ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA181);
        btnFotPreA181.setBounds(880, 816, 40, 40);

        txtPreA182.setEditable(false);
        txtPreA182.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA182.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA182);
        txtPreA182.setBounds(704, 875, 109, 28);

        jtbAnoPreA182.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA182.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA182ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA182);
        jtbAnoPreA182.setBounds(824, 868, 40, 40);

        btnFotPreA182.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA182.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA182ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA182);
        btnFotPreA182.setBounds(880, 868, 40, 40);

        jtaAnoA182.setEditable(false);
        jtaAnoA182.setColumns(20);
        jtaAnoA182.setLineWrap(true);
        jtaAnoA182.setRows(5);
        jspAnoPreA182.setViewportView(jtaAnoA182);

        jpaAba6.add(jspAnoPreA182);
        jspAnoPreA182.setBounds(750, 906, 40, 10);

        txtPreA183.setEditable(false);
        txtPreA183.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA183.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA183);
        txtPreA183.setBounds(704, 928, 109, 28);

        jtbAnoPreA183.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA183.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA183ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA183);
        jtbAnoPreA183.setBounds(824, 922, 40, 40);

        btnFotPreA183.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA183.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA183ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA183);
        btnFotPreA183.setBounds(880, 922, 40, 40);

        jtaAnoA183.setEditable(false);
        jtaAnoA183.setColumns(20);
        jtaAnoA183.setLineWrap(true);
        jtaAnoA183.setRows(5);
        jspAnoPreA183.setViewportView(jtaAnoA183);

        jpaAba6.add(jspAnoPreA183);
        jspAnoPreA183.setBounds(750, 959, 40, 10);

        txtPreA184.setEditable(false);
        txtPreA184.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA184.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA184);
        txtPreA184.setBounds(704, 980, 109, 28);

        jtbAnoPreA184.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA184.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA184ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA184);
        jtbAnoPreA184.setBounds(824, 974, 40, 40);

        btnFotPreA184.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA184.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA184ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA184);
        btnFotPreA184.setBounds(880, 974, 40, 40);

        jtaAnoA184.setEditable(false);
        jtaAnoA184.setColumns(20);
        jtaAnoA184.setLineWrap(true);
        jtaAnoA184.setRows(5);
        jspAnoPreA184.setViewportView(jtaAnoA184);

        jpaAba6.add(jspAnoPreA184);
        jspAnoPreA184.setBounds(750, 1012, 40, 10);

        txtPreA185.setEditable(false);
        txtPreA185.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA185.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA185);
        txtPreA185.setBounds(704, 1034, 109, 28);

        jtbAnoPreA185.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA185.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA185ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA185);
        jtbAnoPreA185.setBounds(824, 1028, 40, 40);

        btnFotPreA185.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA185.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA185ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA185);
        btnFotPreA185.setBounds(880, 1028, 40, 40);

        jtaAnoA185.setEditable(false);
        jtaAnoA185.setColumns(20);
        jtaAnoA185.setLineWrap(true);
        jtaAnoA185.setRows(5);
        jspAnoPreA185.setViewportView(jtaAnoA185);

        jpaAba6.add(jspAnoPreA185);
        jspAnoPreA185.setBounds(750, 1065, 40, 10);

        txtPreA186.setEditable(false);
        txtPreA186.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA186.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA186);
        txtPreA186.setBounds(704, 1086, 109, 28);

        jtbAnoPreA186.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA186.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA186ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA186);
        jtbAnoPreA186.setBounds(824, 1080, 40, 40);

        btnFotPreA186.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA186.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA186ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA186);
        btnFotPreA186.setBounds(880, 1080, 40, 40);

        jtaAnoA186.setEditable(false);
        jtaAnoA186.setColumns(20);
        jtaAnoA186.setLineWrap(true);
        jtaAnoA186.setRows(5);
        jspAnoPreA186.setViewportView(jtaAnoA186);

        jpaAba6.add(jspAnoPreA186);
        jspAnoPreA186.setBounds(750, 1118, 40, 10);

        txtPreA187.setEditable(false);
        txtPreA187.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA187.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA187);
        txtPreA187.setBounds(704, 1140, 109, 28);

        jtbAnoPreA187.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA187.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA187ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA187);
        jtbAnoPreA187.setBounds(824, 1133, 40, 40);

        btnFotPreA187.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA187.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA187ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA187);
        btnFotPreA187.setBounds(880, 1133, 40, 40);

        jtaAnoA187.setEditable(false);
        jtaAnoA187.setColumns(20);
        jtaAnoA187.setLineWrap(true);
        jtaAnoA187.setRows(5);
        jspAnoPreA187.setViewportView(jtaAnoA187);

        jpaAba6.add(jspAnoPreA187);
        jspAnoPreA187.setBounds(750, 1171, 40, 10);

        txtPreA188.setEditable(false);
        txtPreA188.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA188.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA188);
        txtPreA188.setBounds(704, 1192, 109, 28);

        jtbAnoPreA188.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA188.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA188ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA188);
        jtbAnoPreA188.setBounds(824, 1186, 40, 40);

        btnFotPreA188.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA188.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA188ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA188);
        btnFotPreA188.setBounds(880, 1186, 40, 40);

        jtaAnoA188.setEditable(false);
        jtaAnoA188.setColumns(20);
        jtaAnoA188.setLineWrap(true);
        jtaAnoA188.setRows(5);
        jspAnoPreA188.setViewportView(jtaAnoA188);

        jpaAba6.add(jspAnoPreA188);
        jspAnoPreA188.setBounds(750, 1223, 40, 10);

        jtbAnoPreA189.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA189.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA189ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA189);
        jtbAnoPreA189.setBounds(824, 1240, 40, 40);

        btnFotPreA189.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA189.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA189ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA189);
        btnFotPreA189.setBounds(880, 1240, 40, 40);

        txtPreA189.setEditable(false);
        txtPreA189.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA189.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA189);
        txtPreA189.setBounds(704, 1246, 109, 28);

        jtaAnoA189.setEditable(false);
        jtaAnoA189.setColumns(20);
        jtaAnoA189.setLineWrap(true);
        jtaAnoA189.setRows(5);
        jspAnoPreA189.setViewportView(jtaAnoA189);

        jpaAba6.add(jspAnoPreA189);
        jspAnoPreA189.setBounds(750, 1276, 40, 10);

        jtaAnoA190.setEditable(false);
        jtaAnoA190.setColumns(20);
        jtaAnoA190.setLineWrap(true);
        jtaAnoA190.setRows(5);
        jspAnoPreA190.setViewportView(jtaAnoA190);

        jpaAba6.add(jspAnoPreA190);
        jspAnoPreA190.setBounds(750, 1329, 40, 10);

        txtPreA190.setEditable(false);
        txtPreA190.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA190.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA190);
        txtPreA190.setBounds(704, 1298, 109, 28);

        jtbAnoPreA190.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA190.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA190ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA190);
        jtbAnoPreA190.setBounds(824, 1292, 40, 40);

        btnFotPreA190.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA190.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA190ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA190);
        btnFotPreA190.setBounds(880, 1292, 40, 40);

        txtPreA191.setEditable(false);
        txtPreA191.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA191.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA191);
        txtPreA191.setBounds(704, 1352, 109, 28);

        jtbAnoPreA191.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA191.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA191ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA191);
        jtbAnoPreA191.setBounds(824, 1345, 40, 40);

        btnFotPreA191.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA191.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA191ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA191);
        btnFotPreA191.setBounds(880, 1345, 40, 40);

        jtaAnoA191.setEditable(false);
        jtaAnoA191.setColumns(20);
        jtaAnoA191.setLineWrap(true);
        jtaAnoA191.setRows(5);
        jspAnoPreA191.setViewportView(jtaAnoA191);

        jpaAba6.add(jspAnoPreA191);
        jspAnoPreA191.setBounds(750, 1383, 40, 10);

        txtPreA192.setEditable(false);
        txtPreA192.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA192.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA192);
        txtPreA192.setBounds(704, 1406, 109, 28);

        jtbAnoPreA192.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA192.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA192ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA192);
        jtbAnoPreA192.setBounds(824, 1398, 40, 40);

        btnFotPreA192.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA192.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA192ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA192);
        btnFotPreA192.setBounds(880, 1398, 40, 40);

        jtaAnoA192.setEditable(false);
        jtaAnoA192.setColumns(20);
        jtaAnoA192.setLineWrap(true);
        jtaAnoA192.setRows(5);
        jspAnoPreA192.setViewportView(jtaAnoA192);

        jpaAba6.add(jspAnoPreA192);
        jspAnoPreA192.setBounds(750, 1436, 40, 10);

        txtPreA193.setEditable(false);
        txtPreA193.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA193.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA193);
        txtPreA193.setBounds(704, 1458, 109, 28);

        jtbAnoPreA193.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA193.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA193ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA193);
        jtbAnoPreA193.setBounds(824, 1452, 40, 40);

        btnFotPreA193.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA193.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA193ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA193);
        btnFotPreA193.setBounds(880, 1452, 40, 40);

        jtaAnoA193.setEditable(false);
        jtaAnoA193.setColumns(20);
        jtaAnoA193.setLineWrap(true);
        jtaAnoA193.setRows(5);
        jspAnoPreA193.setViewportView(jtaAnoA193);

        jpaAba6.add(jspAnoPreA193);
        jspAnoPreA193.setBounds(750, 1489, 40, 10);

        txtPreA194.setEditable(false);
        txtPreA194.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA194.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA194);
        txtPreA194.setBounds(704, 1510, 109, 28);

        jtbAnoPreA194.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA194.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA194ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA194);
        jtbAnoPreA194.setBounds(824, 1505, 40, 40);

        btnFotPreA194.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA194.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA194ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA194);
        btnFotPreA194.setBounds(880, 1505, 40, 40);

        jtaAnoA194.setEditable(false);
        jtaAnoA194.setColumns(20);
        jtaAnoA194.setLineWrap(true);
        jtaAnoA194.setRows(5);
        jspAnoPreA194.setViewportView(jtaAnoA194);

        jpaAba6.add(jspAnoPreA194);
        jspAnoPreA194.setBounds(750, 1542, 40, 10);

        txtPreA195.setEditable(false);
        txtPreA195.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA195.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA195);
        txtPreA195.setBounds(704, 1564, 109, 28);

        jtbAnoPreA195.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA195.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA195ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA195);
        jtbAnoPreA195.setBounds(824, 1557, 40, 40);

        btnFotPreA195.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA195.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA195ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA195);
        btnFotPreA195.setBounds(880, 1557, 40, 40);

        jtaAnoA195.setEditable(false);
        jtaAnoA195.setColumns(20);
        jtaAnoA195.setLineWrap(true);
        jtaAnoA195.setRows(5);
        jspAnoPreA195.setViewportView(jtaAnoA195);

        jpaAba6.add(jspAnoPreA195);
        jspAnoPreA195.setBounds(750, 1595, 40, 10);

        txtPreA196.setEditable(false);
        txtPreA196.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA196.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA196);
        txtPreA196.setBounds(704, 1616, 109, 28);

        jtbAnoPreA196.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA196.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA196ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA196);
        jtbAnoPreA196.setBounds(824, 1610, 40, 40);

        btnFotPreA196.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA196.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA196ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA196);
        btnFotPreA196.setBounds(880, 1610, 40, 40);

        jtaAnoA196.setEditable(false);
        jtaAnoA196.setColumns(20);
        jtaAnoA196.setLineWrap(true);
        jtaAnoA196.setRows(5);
        jspAnoPreA196.setViewportView(jtaAnoA196);

        jpaAba6.add(jspAnoPreA196);
        jspAnoPreA196.setBounds(750, 1648, 40, 10);

        txtPreA197.setEditable(false);
        txtPreA197.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA197.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA197);
        txtPreA197.setBounds(704, 1670, 109, 28);

        jtaAnoA197.setEditable(false);
        jtaAnoA197.setColumns(20);
        jtaAnoA197.setLineWrap(true);
        jtaAnoA197.setRows(5);
        jspAnoPreA197.setViewportView(jtaAnoA197);

        jpaAba6.add(jspAnoPreA197);
        jspAnoPreA197.setBounds(750, 1701, 40, 10);

        jtbAnoPreA197.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA197.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA197ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA197);
        jtbAnoPreA197.setBounds(824, 1663, 40, 40);

        btnFotPreA197.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA197.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA197ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA197);
        btnFotPreA197.setBounds(880, 1663, 40, 40);

        txtPreA198.setEditable(false);
        txtPreA198.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA198.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA198);
        txtPreA198.setBounds(704, 1723, 109, 28);

        jtbAnoPreA198.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA198.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA198ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA198);
        jtbAnoPreA198.setBounds(824, 1716, 40, 40);

        btnFotPreA198.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA198.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA198ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA198);
        btnFotPreA198.setBounds(880, 1716, 40, 40);

        jtaAnoA198.setEditable(false);
        jtaAnoA198.setColumns(20);
        jtaAnoA198.setLineWrap(true);
        jtaAnoA198.setRows(5);
        jspAnoPreA198.setViewportView(jtaAnoA198);

        jpaAba6.add(jspAnoPreA198);
        jspAnoPreA198.setBounds(750, 1754, 40, 10);

        txtPreA199.setEditable(false);
        txtPreA199.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA199.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA199);
        txtPreA199.setBounds(704, 1776, 109, 28);

        jtbAnoPreA199.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA199.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA199ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA199);
        jtbAnoPreA199.setBounds(824, 1770, 40, 40);

        btnFotPreA199.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA199.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA199ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA199);
        btnFotPreA199.setBounds(880, 1770, 40, 40);

        jtaAnoA199.setEditable(false);
        jtaAnoA199.setColumns(20);
        jtaAnoA199.setLineWrap(true);
        jtaAnoA199.setRows(5);
        jspAnoPreA199.setViewportView(jtaAnoA199);

        jpaAba6.add(jspAnoPreA199);
        jspAnoPreA199.setBounds(750, 1807, 40, 10);

        txtPreA200.setEditable(false);
        txtPreA200.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA200.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA200);
        txtPreA200.setBounds(704, 1828, 109, 28);

        jtbAnoPreA200.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA200ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA200);
        jtbAnoPreA200.setBounds(824, 1823, 40, 40);

        btnFotPreA200.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA200ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA200);
        btnFotPreA200.setBounds(880, 1823, 40, 40);

        jtaAnoA200.setEditable(false);
        jtaAnoA200.setColumns(20);
        jtaAnoA200.setLineWrap(true);
        jtaAnoA200.setRows(5);
        jspAnoPreA200.setViewportView(jtaAnoA200);

        jpaAba6.add(jspAnoPreA200);
        jspAnoPreA200.setBounds(750, 1860, 40, 10);

        txtPreA201.setEditable(false);
        txtPreA201.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA201.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA201);
        txtPreA201.setBounds(704, 1882, 109, 28);

        jtbAnoPreA201.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA201.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA201ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA201);
        jtbAnoPreA201.setBounds(824, 1876, 40, 40);

        btnFotPreA201.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA201.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA201ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA201);
        btnFotPreA201.setBounds(880, 1876, 40, 40);

        jtaAnoA201.setEditable(false);
        jtaAnoA201.setColumns(20);
        jtaAnoA201.setLineWrap(true);
        jtaAnoA201.setRows(5);
        jspAnoPreA201.setViewportView(jtaAnoA201);

        jpaAba6.add(jspAnoPreA201);
        jspAnoPreA201.setBounds(750, 1913, 40, 10);

        txtPreA202.setEditable(false);
        txtPreA202.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA202.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba6.add(txtPreA202);
        txtPreA202.setBounds(704, 1934, 109, 28);

        jtbAnoPreA202.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA202.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA202ActionPerformed(evt);
            }
        });
        jpaAba6.add(jtbAnoPreA202);
        jtbAnoPreA202.setBounds(824, 1928, 40, 40);

        btnFotPreA202.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA202.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA202ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnFotPreA202);
        btnFotPreA202.setBounds(880, 1928, 40, 40);

        jtaAnoA202.setEditable(false);
        jtaAnoA202.setColumns(20);
        jtaAnoA202.setLineWrap(true);
        jtaAnoA202.setRows(5);
        jspAnoPreA202.setViewportView(jtaAnoA202);

        jpaAba6.add(jspAnoPreA202);
        jspAnoPreA202.setBounds(750, 1966, 40, 10);

        btnImpPre5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre5.setToolTipText("Imprimir Documento");
        btnImpPre5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPre5ActionPerformed(evt);
            }
        });
        jpaAba6.add(btnImpPre5);
        btnImpPre5.setBounds(822, 2087, 64, 64);

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/hidráulica trimestral.png"))); // NOI18N
        jpaAba6.add(jLabel23);
        jLabel23.setBounds(-12, 0, 1000, 2225);

        jScrollPane12.setViewportView(jpaAba6);

        jtpAbas1.addTab("Hidráulica Trimestral", jScrollPane12);

        jpaAba5.setPreferredSize(new java.awt.Dimension(960, 2390));
        jpaAba5.setLayout(null);

        txtIdPre6.setEditable(false);
        txtIdPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtIdPre6);
        txtIdPre6.setBounds(58, 182, 100, 28);

        txtDatIni6.setEditable(false);
        txtDatIni6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtDatIni6);
        txtDatIni6.setBounds(188, 182, 170, 28);

        txtDatFin6.setEditable(false);
        txtDatFin6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtDatFin6);
        txtDatFin6.setBounds(388, 182, 170, 28);

        txtTemPre6.setEditable(false);
        txtTemPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtTemPre6);
        txtTemPre6.setBounds(588, 182, 170, 28);

        txtDatPre6.setEditable(false);
        txtDatPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtDatPre6);
        txtDatPre6.setBounds(788, 182, 140, 28);

        txtNomTec6.setEditable(false);
        txtNomTec6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtNomTec6);
        txtNomTec6.setBounds(58, 243, 180, 28);

        jScrollPane18.setEnabled(false);

        jtaEquPre6.setEditable(false);
        jtaEquPre6.setColumns(20);
        jtaEquPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre6.setLineWrap(true);
        jtaEquPre6.setRows(5);
        jScrollPane18.setViewportView(jtaEquPre6);

        jpaAba5.add(jScrollPane18);
        jScrollPane18.setBounds(477, 238, 184, 38);

        jScrollPane19.setEnabled(false);

        jtaSetPre6.setEditable(false);
        jtaSetPre6.setColumns(20);
        jtaSetPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre6.setLineWrap(true);
        jtaSetPre6.setRows(5);
        jScrollPane19.setViewportView(jtaSetPre6);

        jpaAba5.add(jScrollPane19);
        jScrollPane19.setBounds(266, 238, 184, 38);

        txtCodEqu6.setEditable(false);
        txtCodEqu6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtCodEqu6);
        txtCodEqu6.setBounds(688, 243, 100, 28);

        txtStaPre6.setEditable(false);
        txtStaPre6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtStaPre6);
        txtStaPre6.setBounds(818, 243, 110, 28);

        jtbAnoPreA203.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA203.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA203ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA203);
        jtbAnoPreA203.setBounds(824, 391, 40, 40);

        jtaAnoA203.setEditable(false);
        jtaAnoA203.setColumns(20);
        jtaAnoA203.setLineWrap(true);
        jtaAnoA203.setRows(5);
        jspAnoPreA203.setViewportView(jtaAnoA203);

        jpaAba5.add(jspAnoPreA203);
        jspAnoPreA203.setBounds(750, 428, 40, 10);

        btnFotPreA203.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA203.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA203ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA203);
        btnFotPreA203.setBounds(880, 391, 40, 40);

        txtPreA203.setEditable(false);
        txtPreA203.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA203.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA203);
        txtPreA203.setBounds(704, 397, 109, 28);

        jtaAnoA204.setEditable(false);
        jtaAnoA204.setColumns(20);
        jtaAnoA204.setLineWrap(true);
        jtaAnoA204.setRows(5);
        jspAnoPreA204.setViewportView(jtaAnoA204);

        jpaAba5.add(jspAnoPreA204);
        jspAnoPreA204.setBounds(750, 481, 40, 10);

        txtPreA204.setEditable(false);
        txtPreA204.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA204.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA204);
        txtPreA204.setBounds(704, 450, 109, 28);

        jtbAnoPreA204.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA204.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA204ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA204);
        jtbAnoPreA204.setBounds(824, 444, 40, 40);

        btnFotPreA204.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA204.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA204ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA204);
        btnFotPreA204.setBounds(880, 444, 40, 40);

        txtPreA205.setEditable(false);
        txtPreA205.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA205.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA205);
        txtPreA205.setBounds(704, 504, 109, 28);

        jtbAnoPreA205.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA205.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA205ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA205);
        jtbAnoPreA205.setBounds(824, 498, 40, 40);

        btnFotPreA205.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA205.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA205ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA205);
        btnFotPreA205.setBounds(880, 498, 40, 40);

        jtaAnoA205.setEditable(false);
        jtaAnoA205.setColumns(20);
        jtaAnoA205.setLineWrap(true);
        jtaAnoA205.setRows(5);
        jspAnoPreA205.setViewportView(jtaAnoA205);

        jpaAba5.add(jspAnoPreA205);
        jspAnoPreA205.setBounds(750, 535, 40, 10);

        txtPreA206.setEditable(false);
        txtPreA206.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA206.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA206);
        txtPreA206.setBounds(704, 556, 109, 28);

        jtbAnoPreA206.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA206.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA206ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA206);
        jtbAnoPreA206.setBounds(824, 550, 40, 40);

        btnFotPreA206.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA206.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA206ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA206);
        btnFotPreA206.setBounds(880, 550, 40, 40);

        jtaAnoA206.setEditable(false);
        jtaAnoA206.setColumns(20);
        jtaAnoA206.setLineWrap(true);
        jtaAnoA206.setRows(5);
        jspAnoPreA206.setViewportView(jtaAnoA206);

        jpaAba5.add(jspAnoPreA206);
        jspAnoPreA206.setBounds(750, 587, 40, 10);

        txtPreA207.setEditable(false);
        txtPreA207.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA207.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA207);
        txtPreA207.setBounds(704, 610, 109, 28);

        jtbAnoPreA207.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA207.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA207ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA207);
        jtbAnoPreA207.setBounds(824, 603, 40, 40);

        btnFotPreA207.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA207.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA207ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA207);
        btnFotPreA207.setBounds(880, 603, 40, 40);

        jtaAnoA207.setEditable(false);
        jtaAnoA207.setColumns(20);
        jtaAnoA207.setLineWrap(true);
        jtaAnoA207.setRows(5);
        jspAnoPreA207.setViewportView(jtaAnoA207);

        jpaAba5.add(jspAnoPreA207);
        jspAnoPreA207.setBounds(750, 640, 40, 10);

        txtPreA208.setEditable(false);
        txtPreA208.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA208.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA208);
        txtPreA208.setBounds(704, 660, 109, 28);

        jtbAnoPreA208.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA208.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA208ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA208);
        jtbAnoPreA208.setBounds(824, 656, 40, 40);

        btnFotPreA208.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA208.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA208ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA208);
        btnFotPreA208.setBounds(880, 656, 40, 40);

        jtaAnoA208.setEditable(false);
        jtaAnoA208.setColumns(20);
        jtaAnoA208.setLineWrap(true);
        jtaAnoA208.setRows(5);
        jspAnoPreA208.setViewportView(jtaAnoA208);

        jpaAba5.add(jspAnoPreA208);
        jspAnoPreA208.setBounds(750, 693, 40, 10);

        txtPreA209.setEditable(false);
        txtPreA209.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA209.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA209);
        txtPreA209.setBounds(704, 716, 109, 28);

        jtbAnoPreA209.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA209.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA209ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA209);
        jtbAnoPreA209.setBounds(824, 709, 40, 40);

        btnFotPreA209.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA209.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA209ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA209);
        btnFotPreA209.setBounds(880, 709, 40, 40);

        jtaAnoA209.setEditable(false);
        jtaAnoA209.setColumns(20);
        jtaAnoA209.setLineWrap(true);
        jtaAnoA209.setRows(5);
        jspAnoPreA209.setViewportView(jtaAnoA209);

        jpaAba5.add(jspAnoPreA209);
        jspAnoPreA209.setBounds(750, 746, 40, 10);

        txtPreA210.setEditable(false);
        txtPreA210.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA210.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA210);
        txtPreA210.setBounds(704, 770, 109, 28);

        jtbAnoPreA210.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA210.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA210ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA210);
        jtbAnoPreA210.setBounds(824, 763, 40, 40);

        btnFotPreA210.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA210.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA210ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA210);
        btnFotPreA210.setBounds(880, 763, 40, 40);

        jtaAnoA210.setEditable(false);
        jtaAnoA210.setColumns(20);
        jtaAnoA210.setLineWrap(true);
        jtaAnoA210.setRows(5);
        jspAnoPreA210.setViewportView(jtaAnoA210);

        jpaAba5.add(jspAnoPreA210);
        jspAnoPreA210.setBounds(750, 800, 40, 10);

        txtPreA211.setEditable(false);
        txtPreA211.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA211.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA211);
        txtPreA211.setBounds(704, 822, 109, 28);

        jtaAnoA211.setEditable(false);
        jtaAnoA211.setColumns(20);
        jtaAnoA211.setLineWrap(true);
        jtaAnoA211.setRows(5);
        jspAnoPreA211.setViewportView(jtaAnoA211);

        jpaAba5.add(jspAnoPreA211);
        jspAnoPreA211.setBounds(750, 853, 40, 10);

        jtbAnoPreA211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA211.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA211ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA211);
        jtbAnoPreA211.setBounds(824, 816, 40, 40);

        btnFotPreA211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA211.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA211ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA211);
        btnFotPreA211.setBounds(880, 816, 40, 40);

        txtPreA212.setEditable(false);
        txtPreA212.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA212.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA212);
        txtPreA212.setBounds(704, 875, 109, 28);

        jtbAnoPreA212.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA212.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA212ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA212);
        jtbAnoPreA212.setBounds(824, 868, 40, 40);

        btnFotPreA212.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA212.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA212ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA212);
        btnFotPreA212.setBounds(880, 868, 40, 40);

        jtaAnoA212.setEditable(false);
        jtaAnoA212.setColumns(20);
        jtaAnoA212.setLineWrap(true);
        jtaAnoA212.setRows(5);
        jspAnoPreA212.setViewportView(jtaAnoA212);

        jpaAba5.add(jspAnoPreA212);
        jspAnoPreA212.setBounds(750, 906, 40, 10);

        txtPreA213.setEditable(false);
        txtPreA213.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA213.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA213);
        txtPreA213.setBounds(704, 928, 109, 28);

        jtbAnoPreA213.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA213.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA213ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA213);
        jtbAnoPreA213.setBounds(824, 922, 40, 40);

        btnFotPreA213.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA213.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA213ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA213);
        btnFotPreA213.setBounds(880, 922, 40, 40);

        jtaAnoA213.setEditable(false);
        jtaAnoA213.setColumns(20);
        jtaAnoA213.setLineWrap(true);
        jtaAnoA213.setRows(5);
        jspAnoPreA213.setViewportView(jtaAnoA213);

        jpaAba5.add(jspAnoPreA213);
        jspAnoPreA213.setBounds(750, 959, 40, 10);

        txtPreA214.setEditable(false);
        txtPreA214.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA214.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA214);
        txtPreA214.setBounds(704, 980, 109, 28);

        jtbAnoPreA214.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA214.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA214ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA214);
        jtbAnoPreA214.setBounds(824, 974, 40, 40);

        btnFotPreA214.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA214.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA214ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA214);
        btnFotPreA214.setBounds(880, 974, 40, 40);

        jtaAnoA214.setEditable(false);
        jtaAnoA214.setColumns(20);
        jtaAnoA214.setLineWrap(true);
        jtaAnoA214.setRows(5);
        jspAnoPreA214.setViewportView(jtaAnoA214);

        jpaAba5.add(jspAnoPreA214);
        jspAnoPreA214.setBounds(750, 1012, 40, 10);

        txtPreA215.setEditable(false);
        txtPreA215.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA215.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA215);
        txtPreA215.setBounds(704, 1034, 109, 28);

        jtbAnoPreA215.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA215.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA215ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA215);
        jtbAnoPreA215.setBounds(824, 1028, 40, 40);

        btnFotPreA215.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA215.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA215ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA215);
        btnFotPreA215.setBounds(880, 1028, 40, 40);

        jtaAnoA215.setEditable(false);
        jtaAnoA215.setColumns(20);
        jtaAnoA215.setLineWrap(true);
        jtaAnoA215.setRows(5);
        jspAnoPreA215.setViewportView(jtaAnoA215);

        jpaAba5.add(jspAnoPreA215);
        jspAnoPreA215.setBounds(750, 1065, 40, 10);

        txtPreA216.setEditable(false);
        txtPreA216.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA216.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA216);
        txtPreA216.setBounds(704, 1086, 109, 28);

        jtbAnoPreA216.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA216.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA216ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA216);
        jtbAnoPreA216.setBounds(824, 1080, 40, 40);

        btnFotPreA216.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA216.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA216ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA216);
        btnFotPreA216.setBounds(880, 1080, 40, 40);

        jtaAnoA216.setEditable(false);
        jtaAnoA216.setColumns(20);
        jtaAnoA216.setLineWrap(true);
        jtaAnoA216.setRows(5);
        jspAnoPreA216.setViewportView(jtaAnoA216);

        jpaAba5.add(jspAnoPreA216);
        jspAnoPreA216.setBounds(750, 1118, 40, 10);

        txtPreA217.setEditable(false);
        txtPreA217.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA217.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA217);
        txtPreA217.setBounds(704, 1140, 109, 28);

        jtbAnoPreA217.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA217.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA217ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA217);
        jtbAnoPreA217.setBounds(824, 1133, 40, 40);

        btnFotPreA217.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA217.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA217ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA217);
        btnFotPreA217.setBounds(880, 1133, 40, 40);

        jtaAnoA217.setEditable(false);
        jtaAnoA217.setColumns(20);
        jtaAnoA217.setLineWrap(true);
        jtaAnoA217.setRows(5);
        jspAnoPreA217.setViewportView(jtaAnoA217);

        jpaAba5.add(jspAnoPreA217);
        jspAnoPreA217.setBounds(750, 1171, 40, 10);

        txtPreA218.setEditable(false);
        txtPreA218.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA218.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA218);
        txtPreA218.setBounds(704, 1192, 109, 28);

        jtbAnoPreA218.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA218.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA218ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA218);
        jtbAnoPreA218.setBounds(824, 1186, 40, 40);

        btnFotPreA218.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA218.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA218ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA218);
        btnFotPreA218.setBounds(880, 1186, 40, 40);

        jtaAnoA218.setEditable(false);
        jtaAnoA218.setColumns(20);
        jtaAnoA218.setLineWrap(true);
        jtaAnoA218.setRows(5);
        jspAnoPreA218.setViewportView(jtaAnoA218);

        jpaAba5.add(jspAnoPreA218);
        jspAnoPreA218.setBounds(750, 1223, 40, 10);

        jtbAnoPreA219.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA219.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA219ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA219);
        jtbAnoPreA219.setBounds(824, 1240, 40, 40);

        btnFotPreA219.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA219.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA219ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA219);
        btnFotPreA219.setBounds(880, 1240, 40, 40);

        jtaAnoA219.setEditable(false);
        jtaAnoA219.setColumns(20);
        jtaAnoA219.setLineWrap(true);
        jtaAnoA219.setRows(5);
        jspAnoPreA219.setViewportView(jtaAnoA219);

        jpaAba5.add(jspAnoPreA219);
        jspAnoPreA219.setBounds(750, 1276, 40, 10);

        txtPreA219.setEditable(false);
        txtPreA219.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA219.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA219);
        txtPreA219.setBounds(704, 1246, 109, 28);

        jtaAnoA220.setEditable(false);
        jtaAnoA220.setColumns(20);
        jtaAnoA220.setLineWrap(true);
        jtaAnoA220.setRows(5);
        jspAnoPreA220.setViewportView(jtaAnoA220);

        jpaAba5.add(jspAnoPreA220);
        jspAnoPreA220.setBounds(750, 1329, 40, 10);

        txtPreA220.setEditable(false);
        txtPreA220.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA220.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA220);
        txtPreA220.setBounds(704, 1298, 109, 28);

        jtbAnoPreA220.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA220.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA220ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA220);
        jtbAnoPreA220.setBounds(824, 1292, 40, 40);

        btnFotPreA220.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA220.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA220ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA220);
        btnFotPreA220.setBounds(880, 1292, 40, 40);

        txtPreA221.setEditable(false);
        txtPreA221.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA221.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA221);
        txtPreA221.setBounds(704, 1352, 109, 28);

        jtbAnoPreA221.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA221.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA221ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA221);
        jtbAnoPreA221.setBounds(824, 1345, 40, 40);

        btnFotPreA221.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA221.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA221ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA221);
        btnFotPreA221.setBounds(880, 1345, 40, 40);

        jtaAnoA221.setEditable(false);
        jtaAnoA221.setColumns(20);
        jtaAnoA221.setLineWrap(true);
        jtaAnoA221.setRows(5);
        jspAnoPreA221.setViewportView(jtaAnoA221);

        jpaAba5.add(jspAnoPreA221);
        jspAnoPreA221.setBounds(750, 1383, 40, 10);

        txtPreA222.setEditable(false);
        txtPreA222.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA222.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA222);
        txtPreA222.setBounds(704, 1406, 109, 28);

        jtbAnoPreA222.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA222.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA222ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA222);
        jtbAnoPreA222.setBounds(824, 1398, 40, 40);

        btnFotPreA222.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA222.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA222ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA222);
        btnFotPreA222.setBounds(880, 1398, 40, 40);

        jtaAnoA222.setEditable(false);
        jtaAnoA222.setColumns(20);
        jtaAnoA222.setLineWrap(true);
        jtaAnoA222.setRows(5);
        jspAnoPreA222.setViewportView(jtaAnoA222);

        jpaAba5.add(jspAnoPreA222);
        jspAnoPreA222.setBounds(750, 1436, 40, 10);

        txtPreA223.setEditable(false);
        txtPreA223.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA223.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA223);
        txtPreA223.setBounds(704, 1458, 109, 28);

        jtbAnoPreA223.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA223.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA223ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA223);
        jtbAnoPreA223.setBounds(824, 1452, 40, 40);

        btnFotPreA223.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA223.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA223ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA223);
        btnFotPreA223.setBounds(880, 1452, 40, 40);

        jtaAnoA223.setEditable(false);
        jtaAnoA223.setColumns(20);
        jtaAnoA223.setLineWrap(true);
        jtaAnoA223.setRows(5);
        jspAnoPreA223.setViewportView(jtaAnoA223);

        jpaAba5.add(jspAnoPreA223);
        jspAnoPreA223.setBounds(750, 1489, 40, 10);

        txtPreA224.setEditable(false);
        txtPreA224.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA224.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA224);
        txtPreA224.setBounds(704, 1510, 109, 28);

        jtbAnoPreA224.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA224.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA224ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA224);
        jtbAnoPreA224.setBounds(824, 1505, 40, 40);

        btnFotPreA224.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA224.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA224ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA224);
        btnFotPreA224.setBounds(880, 1505, 40, 40);

        jtaAnoA224.setEditable(false);
        jtaAnoA224.setColumns(20);
        jtaAnoA224.setLineWrap(true);
        jtaAnoA224.setRows(5);
        jspAnoPreA224.setViewportView(jtaAnoA224);

        jpaAba5.add(jspAnoPreA224);
        jspAnoPreA224.setBounds(750, 1542, 40, 10);

        txtPreA225.setEditable(false);
        txtPreA225.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA225.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA225);
        txtPreA225.setBounds(704, 1564, 109, 28);

        jtbAnoPreA225.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA225.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA225ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA225);
        jtbAnoPreA225.setBounds(824, 1557, 40, 40);

        btnFotPreA225.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA225.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA225ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA225);
        btnFotPreA225.setBounds(880, 1557, 40, 40);

        jtaAnoA225.setEditable(false);
        jtaAnoA225.setColumns(20);
        jtaAnoA225.setLineWrap(true);
        jtaAnoA225.setRows(5);
        jspAnoPreA225.setViewportView(jtaAnoA225);

        jpaAba5.add(jspAnoPreA225);
        jspAnoPreA225.setBounds(750, 1595, 40, 10);

        txtPreA226.setEditable(false);
        txtPreA226.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA226.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA226);
        txtPreA226.setBounds(704, 1616, 109, 28);

        jtbAnoPreA226.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA226.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA226ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA226);
        jtbAnoPreA226.setBounds(824, 1610, 40, 40);

        btnFotPreA226.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA226.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA226ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA226);
        btnFotPreA226.setBounds(880, 1610, 40, 40);

        jtaAnoA226.setEditable(false);
        jtaAnoA226.setColumns(20);
        jtaAnoA226.setLineWrap(true);
        jtaAnoA226.setRows(5);
        jspAnoPreA226.setViewportView(jtaAnoA226);

        jpaAba5.add(jspAnoPreA226);
        jspAnoPreA226.setBounds(750, 1648, 40, 10);

        txtPreA227.setEditable(false);
        txtPreA227.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA227.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA227);
        txtPreA227.setBounds(704, 1670, 109, 28);

        jtaAnoA227.setEditable(false);
        jtaAnoA227.setColumns(20);
        jtaAnoA227.setLineWrap(true);
        jtaAnoA227.setRows(5);
        jspAnoPreA227.setViewportView(jtaAnoA227);

        jpaAba5.add(jspAnoPreA227);
        jspAnoPreA227.setBounds(750, 1701, 40, 10);

        jtbAnoPreA227.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA227.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA227ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA227);
        jtbAnoPreA227.setBounds(824, 1663, 40, 40);

        btnFotPreA227.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA227.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA227ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA227);
        btnFotPreA227.setBounds(880, 1663, 40, 40);

        txtPreA228.setEditable(false);
        txtPreA228.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA228.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA228);
        txtPreA228.setBounds(704, 1723, 109, 28);

        jtbAnoPreA228.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA228.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA228ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA228);
        jtbAnoPreA228.setBounds(824, 1716, 40, 40);

        btnFotPreA228.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA228.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA228ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA228);
        btnFotPreA228.setBounds(880, 1716, 40, 40);

        jtaAnoA228.setEditable(false);
        jtaAnoA228.setColumns(20);
        jtaAnoA228.setLineWrap(true);
        jtaAnoA228.setRows(5);
        jspAnoPreA228.setViewportView(jtaAnoA228);

        jpaAba5.add(jspAnoPreA228);
        jspAnoPreA228.setBounds(750, 1754, 40, 10);

        txtPreA229.setEditable(false);
        txtPreA229.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA229.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA229);
        txtPreA229.setBounds(704, 1776, 109, 28);

        jtbAnoPreA229.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA229.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA229ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA229);
        jtbAnoPreA229.setBounds(824, 1770, 40, 40);

        btnFotPreA229.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA229.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA229ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA229);
        btnFotPreA229.setBounds(880, 1770, 40, 40);

        jtaAnoA229.setEditable(false);
        jtaAnoA229.setColumns(20);
        jtaAnoA229.setLineWrap(true);
        jtaAnoA229.setRows(5);
        jspAnoPreA229.setViewportView(jtaAnoA229);

        jpaAba5.add(jspAnoPreA229);
        jspAnoPreA229.setBounds(750, 1807, 40, 10);

        txtPreA230.setEditable(false);
        txtPreA230.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA230.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA230);
        txtPreA230.setBounds(704, 1828, 109, 28);

        jtbAnoPreA230.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA230.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA230ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA230);
        jtbAnoPreA230.setBounds(824, 1823, 40, 40);

        btnFotPreA230.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA230.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA230ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA230);
        btnFotPreA230.setBounds(880, 1823, 40, 40);

        jtaAnoA230.setEditable(false);
        jtaAnoA230.setColumns(20);
        jtaAnoA230.setLineWrap(true);
        jtaAnoA230.setRows(5);
        jspAnoPreA230.setViewportView(jtaAnoA230);

        jpaAba5.add(jspAnoPreA230);
        jspAnoPreA230.setBounds(750, 1860, 40, 10);

        txtPreA231.setEditable(false);
        txtPreA231.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA231.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA231);
        txtPreA231.setBounds(704, 1882, 109, 28);

        jtbAnoPreA231.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA231.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA231ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA231);
        jtbAnoPreA231.setBounds(824, 1876, 40, 40);

        btnFotPreA231.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA231.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA231ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA231);
        btnFotPreA231.setBounds(880, 1876, 40, 40);

        jtaAnoA231.setEditable(false);
        jtaAnoA231.setColumns(20);
        jtaAnoA231.setLineWrap(true);
        jtaAnoA231.setRows(5);
        jspAnoPreA231.setViewportView(jtaAnoA231);

        jpaAba5.add(jspAnoPreA231);
        jspAnoPreA231.setBounds(750, 1913, 40, 10);

        txtPreA232.setEditable(false);
        txtPreA232.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA232.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA232);
        txtPreA232.setBounds(704, 1934, 109, 28);

        jtbAnoPreA232.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA232.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA232ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA232);
        jtbAnoPreA232.setBounds(824, 1928, 40, 40);

        btnFotPreA232.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA232.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA232ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA232);
        btnFotPreA232.setBounds(880, 1928, 40, 40);

        jtaAnoA232.setEditable(false);
        jtaAnoA232.setColumns(20);
        jtaAnoA232.setLineWrap(true);
        jtaAnoA232.setRows(5);
        jspAnoPreA232.setViewportView(jtaAnoA232);

        jpaAba5.add(jspAnoPreA232);
        jspAnoPreA232.setBounds(750, 1966, 40, 10);

        txtPreA233.setEditable(false);
        txtPreA233.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA233.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA233);
        txtPreA233.setBounds(704, 1988, 109, 28);

        jtbAnoPreA233.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA233.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA233ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA233);
        jtbAnoPreA233.setBounds(824, 1982, 40, 40);

        btnFotPreA233.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA233.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA233ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA233);
        btnFotPreA233.setBounds(880, 1982, 40, 40);

        jtaAnoA233.setEditable(false);
        jtaAnoA233.setColumns(20);
        jtaAnoA233.setLineWrap(true);
        jtaAnoA233.setRows(5);
        jspAnoPreA233.setViewportView(jtaAnoA233);

        jpaAba5.add(jspAnoPreA233);
        jspAnoPreA233.setBounds(750, 2019, 40, 10);

        txtPreA234.setEditable(false);
        txtPreA234.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA234.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA234);
        txtPreA234.setBounds(704, 2040, 109, 28);

        jtbAnoPreA234.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA234.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA234ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA234);
        jtbAnoPreA234.setBounds(824, 2034, 40, 40);

        btnFotPreA234.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA234.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA234ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA234);
        btnFotPreA234.setBounds(880, 2034, 40, 40);

        jtaAnoA234.setEditable(false);
        jtaAnoA234.setColumns(20);
        jtaAnoA234.setLineWrap(true);
        jtaAnoA234.setRows(5);
        jspAnoPreA234.setViewportView(jtaAnoA234);

        jpaAba5.add(jspAnoPreA234);
        jspAnoPreA234.setBounds(750, 2072, 40, 10);

        jtbAnoPreA235.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA235.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA235ActionPerformed(evt);
            }
        });
        jpaAba5.add(jtbAnoPreA235);
        jtbAnoPreA235.setBounds(824, 2088, 40, 40);

        btnFotPreA235.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA235.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA235ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnFotPreA235);
        btnFotPreA235.setBounds(880, 2088, 40, 40);

        txtPreA235.setEditable(false);
        txtPreA235.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA235.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba5.add(txtPreA235);
        txtPreA235.setBounds(704, 2094, 109, 28);

        jtaAnoA235.setEditable(false);
        jtaAnoA235.setColumns(20);
        jtaAnoA235.setLineWrap(true);
        jtaAnoA235.setRows(5);
        jspAnoPreA235.setViewportView(jtaAnoA235);

        jpaAba5.add(jspAnoPreA235);
        jspAnoPreA235.setBounds(750, 2125, 40, 10);

        btnImpPre4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre4.setToolTipText("Imprimir Documento");
        btnImpPre4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPre4ActionPerformed(evt);
            }
        });
        jpaAba5.add(btnImpPre4);
        btnImpPre4.setBounds(822, 2242, 64, 64);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/hidrailica semestre.png"))); // NOI18N
        jpaAba5.add(jLabel22);
        jLabel22.setBounds(-12, 0, 1000, 2390);

        jScrollPane17.setViewportView(jpaAba5);

        jtpAbas1.addTab("Hidráulica Semestral", jScrollPane17);

        getContentPane().add(jtpAbas1);
        jtpAbas1.setBounds(-5, 0, 1005, 632);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened
     *
     * Executado automaticamente quando o `JInternalFrame` é aberto.
     *
     * Funcionalidades: - Oculta o painel de arquivos (`jpaArqPre`) ao iniciar.
     * - Utiliza dois laços para ocultar dinamicamente os campos
     * `jspAnoPreAxxx`: - Primeiro laço: oculta os campos de A119 a A145. -
     * Segundo laço: oculta os campos de A175 a A235. - Para cada campo: - Obtém
     * o nome do campo via reflexão (`getDeclaredField`). - Torna o campo
     * acessível e o converte para `JComponent`. - Define sua visibilidade como
     * `false`. - Em caso de erro (campo inexistente ou inacessível), exibe
     * mensagem de aviso via `JOptionPane`.
     *
     * Observações: - O uso de reflexão permite ocultar campos sem precisar
     * declarar cada um manualmente. - Os intervalos pulam os campos A146 a
     * A174, sugerindo que pertencem a outro tipo de preventiva.
     *
     * Esse método garante que a interface comece limpa e organizada, ocultando
     * campos que só devem ser exibidos conforme a interação do usuário.
     *
     * @param evt o evento gerado pela abertura do `JInternalFrame`
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        jpaArqPre.setVisible(false);

        // Laço para ocultar jspAnoPreA119 a jspAnoPreA145
        for (int i = 119; i <= 145; i++) {
            try {
                String nomeCampo = "jspAnoPreA" + i;
                Field campo = this.getClass().getDeclaredField(nomeCampo);
                campo.setAccessible(true);
                JComponent componente = (JComponent) campo.get(this);
                componente.setVisible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                JOptionPane.showMessageDialog(null, "Erro ao ocultar campo " + i + ": " + e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        }

        // Laço para ocultar jspAnoPreA175 a jspAnoPreA235
        for (int i = 175; i <= 235; i++) {
            try {
                String nomeCampo = "jspAnoPreA" + i;
                Field campo = this.getClass().getDeclaredField(nomeCampo);
                campo.setAccessible(true);
                JComponent componente = (JComponent) campo.get(this);
                componente.setVisible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                JOptionPane.showMessageDialog(null, "Erro ao ocultar campo " + i + ": " + e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        }

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método btnSalFotPre8ActionPerformed
     *
     * Acionado quando o botão de salvar foto preventiva (`btnSalFotPre8`) é
     * clicado.
     *
     * Funcionalidades: - Chama o método `salve_file()` para iniciar o processo
     * de salvamento do arquivo selecionado.
     *
     * Observações: - A lógica de salvamento está encapsulada em `salve_file()`,
     * mantendo o código limpo e modular. - Esse botão só deve estar habilitado
     * quando houver um arquivo válido selecionado.
     *
     * Esse método conecta a ação do usuário à funcionalidade de salvamento,
     * garantindo uma experiência intuitiva e eficiente.
     *
     * @param evt o evento gerado pelo clique no botão de salvar
     */
    private void btnSalFotPre8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalFotPre8ActionPerformed

        salve_file();
    }//GEN-LAST:event_btnSalFotPre8ActionPerformed

    /**
     * Método txtCanFotActionPerformed
     *
     * Acionado quando o botão de cancelar visualização de foto preventiva
     * (`txtCanFot`) é clicado.
     *
     * Funcionalidades: - Oculta o painel de arquivos (`jpaArqPre`). - Remove a
     * imagem exibida (`lblFotEltMen8`) e limpa o campo de nome do arquivo
     * (`txtFotPre8`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Limpa
     * a tabela de arquivos (`tblArqPre`) e os campos de pesquisa (`txtPesArq`,
     * `txtQanArq`).
     *
     * Essa ação garante que a interface seja restaurada ao estado inicial,
     * permitindo ao usuário cancelar a visualização sem deixar resíduos visuais
     * ou dados.
     *
     * @param evt o evento gerado pelo clique no botão de cancelar
     */
    private void txtCanFotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanFotActionPerformed

        jpaArqPre.setVisible(false);
        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        btnSalFotPre8.setEnabled(false);
        ((DefaultTableModel) tblArqPre.getModel()).setRowCount(0);
        txtPesArq.setText(null);
        txtQanArq.setText(null);
    }//GEN-LAST:event_txtCanFotActionPerformed

    /**
     * Método txtPesArqKeyReleased
     *
     * Acionado automaticamente quando o usuário libera uma tecla no campo de
     * pesquisa de arquivos (`txtPesArq`).
     *
     * Funcionalidades: - Chama o método `pesq()` para realizar a busca de
     * arquivos com base no texto digitado. - Permite que a pesquisa seja feita
     * de forma dinâmica, conforme o usuário digita no campo.
     *
     * Observações: - O evento `KeyReleased` é ideal para capturar o momento em
     * que o usuário termina de digitar uma tecla, garantindo que o conteúdo do
     * campo esteja atualizado antes da execução da busca. - Pode ser aprimorado
     * com debounce ou verificação de tamanho mínimo do texto para evitar
     * consultas excessivas.
     *
     * Esse método torna a experiência de busca mais fluida e responsiva,
     * permitindo que os resultados sejam atualizados em tempo real conforme o
     * usuário digita.
     *
     * @param evt o evento gerado pela liberação de uma tecla no campo de
     * pesquisa
     */
    private void txtPesArqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesArqKeyReleased

        pesq();
    }//GEN-LAST:event_txtPesArqKeyReleased

    /**
     * Método tblArqPreMouseClicked
     *
     * Acionado quando o usuário clica em uma linha da tabela de arquivos
     * (`tblArqPre`).
     *
     * Funcionalidades: - Chama o método `setar_arq()` para capturar os dados do
     * arquivo selecionado: - Atualiza os campos `txtFotPre8`, `nome_pre`,
     * `id_prev`. - Prepara a interface para visualização e download. - Chama o
     * método `arq_fotos()` para exibir a imagem correspondente ao arquivo.
     *
     * Observações: - O evento `MouseClicked` permite que a ação ocorra com um
     * único clique. - Pode ser aprimorado com verificação de linha válida ou
     * duplo clique, se necessário.
     *
     * Esse método conecta a interação do usuário com a visualização do conteúdo
     * do arquivo, tornando a navegação mais intuitiva e responsiva.
     *
     * @param evt o evento gerado pelo clique do mouse na tabela
     */
    private void tblArqPreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblArqPreMouseClicked

        setar_arq();
        arq_fotos();
    }//GEN-LAST:event_tblArqPreMouseClicked

    /**
     * Método btnFotPreA202ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A202 da preventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A30"` (referente ao item A202). - Torna
     * o painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama
     * o método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A202, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA202ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA202ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A30";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA202ActionPerformed

    /**
     * Método jtbAnoPreA202ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA202`) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA202`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A202, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA202ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA202ActionPerformed

        if (jtbAnoPreA202.isSelected()) {
            jspAnoPreA202.setVisible(true);
            jspAnoPreA202.setSize(170, 100);
        } else {
            jspAnoPreA202.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA202ActionPerformed

    /**
     * Método btnFotPreA201ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A201 da preventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A29"` (referente ao item A201). - Torna
     * o painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama
     * o método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A201, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA201ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA201ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A29";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA201ActionPerformed

    /**
     * Método jtbAnoPreA201ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA201) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA201`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A201, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA201ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA201ActionPerformed

        if (jtbAnoPreA201.isSelected()) {
            jspAnoPreA201.setVisible(true);
            jspAnoPreA201.setSize(170, 100);
        } else {
            jspAnoPreA201.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA201ActionPerformed

    /**
     * Método btnFotPreA200ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A200 da preventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A28"` (referente ao item A200). - Torna
     * o painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama
     * o método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A200, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA200ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA200ActionPerformed

    /**
     * Método jtbAnoPreA200ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA200) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA200`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A200, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA200ActionPerformed

        if (jtbAnoPreA200.isSelected()) {
            jspAnoPreA200.setVisible(true);
            jspAnoPreA200.setSize(170, 100);
        } else {
            jspAnoPreA200.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA200ActionPerformed

    /**
     * Método btnFotPreA199ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A199 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A27"` (referente ao item A199)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A199, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA199ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA199ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A27";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA199ActionPerformed

    /**
     * Método jtbAnoPreA199ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA199) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA199`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A199, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA199ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA199ActionPerformed

        if (jtbAnoPreA199.isSelected()) {
            jspAnoPreA199.setVisible(true);
            jspAnoPreA199.setSize(170, 100);
        } else {
            jspAnoPreA199.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA199ActionPerformed

    /**
     * Método btnFotPreA198ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A198 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A26"` (referente ao item A198)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A198, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA198ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA198ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA198ActionPerformed

    /**
     * Método jtbAnoPreA198ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA198) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA198`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A198, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA198ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA198ActionPerformed

        if (jtbAnoPreA198.isSelected()) {
            jspAnoPreA198.setVisible(true);
            jspAnoPreA198.setSize(170, 100);
        } else {
            jspAnoPreA198.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA198ActionPerformed

    /**
     * Método btnFotPreA197ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A197 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A25"` (referente ao item A197)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A197, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA197ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA197ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA197ActionPerformed

    /**
     * Método jtbAnoPreA197ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA197) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA197`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A197, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA197ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA197ActionPerformed

        if (jtbAnoPreA197.isSelected()) {
            jspAnoPreA197.setVisible(true);
            jspAnoPreA197.setSize(170, 100);
        } else {
            jspAnoPreA197.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA197ActionPerformed

    /**
     * Método btnFotPreA196ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A196 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A24"` (referente ao item A196)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A196, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA196ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA196ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA196ActionPerformed

    /**
     * Método jtbAnoPreA196ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA196) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA196`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A196, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA196ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA196ActionPerformed

        if (jtbAnoPreA196.isSelected()) {
            jspAnoPreA196.setVisible(true);
            jspAnoPreA196.setSize(170, 100);
        } else {
            jspAnoPreA196.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA196ActionPerformed

    /**
     * Método btnFotPreA195ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A195 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A23"` (referente ao item A195)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A195, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA195ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA195ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA195ActionPerformed

    /**
     * Método jtbAnoPreA195ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA195) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA195`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A195, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA195ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA195ActionPerformed

        if (jtbAnoPreA195.isSelected()) {
            jspAnoPreA195.setVisible(true);
            jspAnoPreA195.setSize(170, 100);
        } else {
            jspAnoPreA195.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA195ActionPerformed

    /**
     * Método btnFotPreA194ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A194 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A22"` (referente ao item A194)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A194, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA194ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA194ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA194ActionPerformed

    /**
     * Método jtbAnoPreA194ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA194) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA195`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A194, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA194ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA194ActionPerformed

        if (jtbAnoPreA194.isSelected()) {
            jspAnoPreA194.setVisible(true);
            jspAnoPreA194.setSize(170, 100);
        } else {
            jspAnoPreA194.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA194ActionPerformed

    /**
     * Método btnFotPreA193ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A193 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A21"` (referente ao item A193)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A193, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA193ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA193ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA193ActionPerformed

    /**
     * Método jtbAnoPreA193ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA193) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA193`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A193, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA193ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA193ActionPerformed

        if (jtbAnoPreA193.isSelected()) {
            jspAnoPreA193.setVisible(true);
            jspAnoPreA193.setSize(170, 100);
        } else {
            jspAnoPreA193.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA193ActionPerformed

    /**
     * Método btnFotPreA192ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A192 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A20"` (referente ao item A192)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A192, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA192ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA192ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA192ActionPerformed

    /**
     * Método jtbAnoPreA192ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA192) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA192`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A192, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA192ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA192ActionPerformed

        if (jtbAnoPreA192.isSelected()) {
            jspAnoPreA192.setVisible(true);
            jspAnoPreA192.setSize(170, 100);
        } else {
            jspAnoPreA192.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA192ActionPerformed

    /**
     * Método btnFotPreA191ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A191 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A19` (referente ao item A191)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A191, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA191ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA191ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA191ActionPerformed

    /**
     * Método jtbAnoPreA191ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA191) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA191`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A191, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA191ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA191ActionPerformed

        if (jtbAnoPreA191.isSelected()) {
            jspAnoPreA191.setVisible(true);
            jspAnoPreA191.setSize(170, 100);
        } else {
            jspAnoPreA191.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA191ActionPerformed

    /**
     * Método btnFotPreA190ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A190 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A18` (referente ao item A190)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A190, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA190ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA190ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA190ActionPerformed

    /**
     * Método jtbAnoPreA190ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA190) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA190`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A190, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA190ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA190ActionPerformed

        if (jtbAnoPreA190.isSelected()) {
            jspAnoPreA190.setVisible(true);
            jspAnoPreA190.setSize(170, 100);
        } else {
            jspAnoPreA190.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA190ActionPerformed

    /**
     * Método btnFotPreA189ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A189 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A17` (referente ao item A189)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A189, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA189ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA189ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA189ActionPerformed

    /**
     * Método jtbAnoPreA189ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA189) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA189`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A189, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA189ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA189ActionPerformed

        if (jtbAnoPreA189.isSelected()) {
            jspAnoPreA189.setVisible(true);
            jspAnoPreA189.setSize(170, 100);
        } else {
            jspAnoPreA189.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA189ActionPerformed

    /**
     * Método btnFotPreA188AtionPerformed
     *
     * Acionado quando o botão relacionado ao item A188 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A16` (referente ao item A188)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A188, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA188ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA188ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA188ActionPerformed

    /**
     * Método jtbAnoPreA188ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA188) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA188`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A188, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA188ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA188ActionPerformed

        if (jtbAnoPreA188.isSelected()) {
            jspAnoPreA188.setVisible(true);
            jspAnoPreA188.setSize(170, 100);
        } else {
            jspAnoPreA188.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA188ActionPerformed

    /**
     * Método btnFotPreA187ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A187 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A15` (referente ao item A187)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A187, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA187ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA187ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA187ActionPerformed

    /**
     * Método jtbAnoPreA187ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA187) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA187`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A187, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA187ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA187ActionPerformed

        if (jtbAnoPreA187.isSelected()) {
            jspAnoPreA187.setVisible(true);
            jspAnoPreA187.setSize(170, 100);
        } else {
            jspAnoPreA187.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA187ActionPerformed

    /**
     * Método btnFotPreA186ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A186 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A14` (referente ao item A186)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A187, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA186ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA186ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA186ActionPerformed

    /**
     * Método jtbAnoPreA186ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA186) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA186`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A186, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA186ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA186ActionPerformed

        if (jtbAnoPreA186.isSelected()) {
            jspAnoPreA186.setVisible(true);
            jspAnoPreA186.setSize(170, 100);
        } else {
            jspAnoPreA186.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA186ActionPerformed

    /**
     * Método btnFotPreA185ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A185 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A13` (referente ao item A185)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A185, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA185ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA185ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA185ActionPerformed

    /**
     * Método jtbAnoPreA185ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA185) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA185`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A185, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA185ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA185ActionPerformed

        if (jtbAnoPreA185.isSelected()) {
            jspAnoPreA185.setVisible(true);
            jspAnoPreA185.setSize(170, 100);
        } else {
            jspAnoPreA185.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA185ActionPerformed

    /**
     * Método btnFotPreA184ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A184 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A12` (referente ao item A184)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A184, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA184ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA184ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA184ActionPerformed

    /**
     * Método jtbAnoPreA184ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA184) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA184`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A184, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA184ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA184ActionPerformed

        if (jtbAnoPreA184.isSelected()) {
            jspAnoPreA184.setVisible(true);
            jspAnoPreA184.setSize(170, 100);
        } else {
            jspAnoPreA184.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA184ActionPerformed

    /**
     * Método btnFotPreA183ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A183 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A11` (referente ao item A183)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A183, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA183ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA183ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA183ActionPerformed

    /**
     * Método jtbAnoPreA183ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA183) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA183`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A183, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA183ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA183ActionPerformed

        if (jtbAnoPreA183.isSelected()) {
            jspAnoPreA183.setVisible(true);
            jspAnoPreA183.setSize(170, 100);
        } else {
            jspAnoPreA183.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA183ActionPerformed

    /**
     * Método btnFotPreA182AtionPerformed
     *
     * Acionado quando o botão relacionado ao item A182 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A10` (referente ao item A182)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A182, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA182ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA182ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA182ActionPerformed

    /**
     * Método jtbAnoPreA182ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA182) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA182`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A182, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA182ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA182ActionPerformed

        if (jtbAnoPreA182.isSelected()) {
            jspAnoPreA182.setVisible(true);
            jspAnoPreA182.setSize(170, 100);
        } else {
            jspAnoPreA182.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA182ActionPerformed

    /**
     * Método btnFotPreA181ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A181 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A9` (referente ao item A181)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A181, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA181ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA181ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA181ActionPerformed

    /**
     * Método jtbAnoPreA181ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA181) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA181`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A181, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA181ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA181ActionPerformed

        if (jtbAnoPreA181.isSelected()) {
            jspAnoPreA181.setVisible(true);
            jspAnoPreA181.setSize(170, 100);
        } else {
            jspAnoPreA181.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA181ActionPerformed

    /**
     * Método btnFotPreA180AtionPerformed
     *
     * Acionado quando o botão relacionado ao item A180 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A8` (referente ao item A180)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A180, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA180ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA180ActionPerformed

    /**
     * Método jtbAnoPreA180ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA180) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA180`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A180, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA180ActionPerformed

        if (jtbAnoPreA180.isSelected()) {
            jspAnoPreA180.setVisible(true);
            jspAnoPreA180.setSize(170, 100);
        } else {
            jspAnoPreA180.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA180ActionPerformed

    /**
     * Método btnFotPreA179ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A179 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A7` (referente ao item A179)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A179, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA179ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA179ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA179ActionPerformed

    /**
     * Método jtbAnoPreA179ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA179) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA179`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A179, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA179ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA179ActionPerformed

        if (jtbAnoPreA179.isSelected()) {
            jspAnoPreA179.setVisible(true);
            jspAnoPreA179.setSize(170, 100);
        } else {
            jspAnoPreA179.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA179ActionPerformed

    /**
     * Método btnFotPreA178AtionPerformed
     *
     * Acionado quando o botão relacionado ao item A178 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A6` (referente ao item A178)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A178, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA178ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA178ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA178ActionPerformed

    /**
     * Método jtbAnoPreA178ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA178) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA178`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A178, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA178ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA178ActionPerformed

        if (jtbAnoPreA178.isSelected()) {
            jspAnoPreA178.setVisible(true);
            jspAnoPreA178.setSize(170, 100);
        } else {
            jspAnoPreA178.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA178ActionPerformed

    /**
     * Método btnFotPreA177ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A177 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A5` (referente ao item A177)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A177, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA177ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA177ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA177ActionPerformed

    /**
     * Método jtbAnoPreA177ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA177) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA177`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A177, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA177ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA177ActionPerformed

        if (jtbAnoPreA177.isSelected()) {
            jspAnoPreA177.setVisible(true);
            jspAnoPreA177.setSize(170, 100);
        } else {
            jspAnoPreA177.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA177ActionPerformed

    /**
     * Método btnFotPreA176ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A176 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A4` (referente ao item A176)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A176, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA176ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA176ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA176ActionPerformed

    /**
     * Método jtbAnoPreA176ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA176) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA176`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A176, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA176ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA176ActionPerformed

        if (jtbAnoPreA176.isSelected()) {
            jspAnoPreA176.setVisible(true);
            jspAnoPreA176.setSize(170, 100);
        } else {
            jspAnoPreA176.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA176ActionPerformed

    /**
     * Método btnFotPreA176ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A176 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A4` (referente ao item A176)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A176, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA175ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA175ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA175ActionPerformed

    /**
     * Método jtbAnoPreA175ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA175) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA175`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A175, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA175ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA175ActionPerformed

        if (jtbAnoPreA175.isSelected()) {
            jspAnoPreA175.setVisible(true);
            jspAnoPreA175.setSize(170, 100);
        } else {
            jspAnoPreA175.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA175ActionPerformed

    /**
     * Método btnFotPreA174ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A174 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A3` (referente ao item A174)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A174, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA174ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA174ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA174ActionPerformed

    /**
     * Método jtbAnoPreA174ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA174) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA174`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A174, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA174ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA174ActionPerformed

        if (jtbAnoPreA174.isSelected()) {
            jspAnoPreA174.setVisible(true);
            jspAnoPreA174.setSize(170, 100);
        } else {
            jspAnoPreA174.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA174ActionPerformed

    /**
     * Método btnFotPreA173ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A173 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A2` (referente ao item A173)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A173, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA173ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA173ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre4();
    }//GEN-LAST:event_btnFotPreA173ActionPerformed

    /**
     * Método jtbAnoPreA173ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA173) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA173`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A173, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA173ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA173ActionPerformed

        if (jtbAnoPreA173.isSelected()) {
            jspAnoPreA173.setVisible(true);
            jspAnoPreA173.setSize(170, 100);
        } else {
            jspAnoPreA173.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA173ActionPerformed

    /**
     * Método btnFotPreA145AtcionPerformed
     *
     * Acionado quando o botão relacionado ao item A145 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A28 (referente ao item A145)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A145, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA145ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA145ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA145ActionPerformed

    /**
     * Método jtbAnoPreA145ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA145) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA145`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A145, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA145ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA145ActionPerformed

        if (jtbAnoPreA145.isSelected()) {
            jspAnoPreA145.setVisible(true);
            jspAnoPreA145.setSize(170, 100);
        } else {
            jspAnoPreA145.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA145ActionPerformed

    /**
     * Método btnFotPreA144ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A144 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A26 (referente ao item A144)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A144, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA144ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA144ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA144ActionPerformed

    /**
     * Método jtbAnoPreA144ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA144) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA144`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A144, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA144ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA144ActionPerformed

        if (jtbAnoPreA144.isSelected()) {
            jspAnoPreA144.setVisible(true);
            jspAnoPreA144.setSize(170, 100);
        } else {
            jspAnoPreA144.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA144ActionPerformed

    /**
     * Método btnFotPreA143ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A143 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A25 (referente ao item A143)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A143, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA143ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA143ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA143ActionPerformed

    /**
     * Método jtbAnoPreA143ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA143) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA143`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A143, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA143ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA143ActionPerformed

        if (jtbAnoPreA143.isSelected()) {
            jspAnoPreA143.setVisible(true);
            jspAnoPreA143.setSize(170, 100);
        } else {
            jspAnoPreA143.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA143ActionPerformed

    /**
     * Método btnFotPreA142ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A142 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A24 (referente ao item A142)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A142, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA142ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA142ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA142ActionPerformed

    /**
     * Método jtbAnoPreA142ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA142) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA142`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A142, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA142ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA142ActionPerformed

        if (jtbAnoPreA142.isSelected()) {
            jspAnoPreA142.setVisible(true);
            jspAnoPreA142.setSize(170, 100);
        } else {
            jspAnoPreA142.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA142ActionPerformed

    /**
     * Método btnFotPreA141ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A141 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A23 (referente ao item A141)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A141, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA141ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA141ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA141ActionPerformed

    /**
     * Método jtbAnoPreA141ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA141) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA141`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A141, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA141ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA141ActionPerformed

        if (jtbAnoPreA141.isSelected()) {
            jspAnoPreA141.setVisible(true);
            jspAnoPreA141.setSize(170, 100);
        } else {
            jspAnoPreA141.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA141ActionPerformed

    /**
     * Método btnFotPreA140ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A140 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A22 (referente ao item A140)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A140, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA140ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA140ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA140ActionPerformed

    /**
     * Método jtbAnoPreA140ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA140) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA140`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A140, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA140ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA140ActionPerformed

        if (jtbAnoPreA140.isSelected()) {
            jspAnoPreA140.setVisible(true);
            jspAnoPreA140.setSize(170, 100);
        } else {
            jspAnoPreA140.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA140ActionPerformed

    /**
     * Método btnFotPreA139ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A139 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A21 (referente ao item A139)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A139, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA139ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA139ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA139ActionPerformed

    /**
     * Método jtbAnoPreA139ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA139) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA139`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A139, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA139ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA139ActionPerformed

        if (jtbAnoPreA139.isSelected()) {
            jspAnoPreA139.setVisible(true);
            jspAnoPreA139.setSize(170, 100);
        } else {
            jspAnoPreA139.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA139ActionPerformed

    /**
     * Método btnFotPreA138ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A138 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A20 (referente ao item A138)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A138, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA138ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA138ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA138ActionPerformed

    /**
     * Método jtbAnoPreA138ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA138) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA138`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A138, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA138ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA138ActionPerformed

        if (jtbAnoPreA138.isSelected()) {
            jspAnoPreA138.setVisible(true);
            jspAnoPreA138.setSize(170, 100);
        } else {
            jspAnoPreA138.setVisible(false);;
        }
    }//GEN-LAST:event_jtbAnoPreA138ActionPerformed

    /**
     * Método btnFotPreA137ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A137 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A19 (referente ao item A137)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A137, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA137ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA137ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA137ActionPerformed

    /**
     * Método jtbAnoPreA137ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA137) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA137`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A137, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA137ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA137ActionPerformed

        if (jtbAnoPreA137.isSelected()) {
            jspAnoPreA137.setVisible(true);
            jspAnoPreA137.setSize(170, 100);
        } else {
            jspAnoPreA137.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA137ActionPerformed

    /**
     * Método btnFotPreA136ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A136 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A18 (referente ao item A136)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A136, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA136ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA136ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA136ActionPerformed

    /**
     * Método jtbAnoPreA136ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA136) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA136`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A136, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA136ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA136ActionPerformed

        if (jtbAnoPreA136.isSelected()) {
            jspAnoPreA136.setVisible(true);
            jspAnoPreA136.setSize(170, 100);
        } else {
            jspAnoPreA136.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA136ActionPerformed

    /**
     * Método btnFotPreA135ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A135 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A17 (referente ao item A135)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A135, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA135ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA135ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA135ActionPerformed

    /**
     * Método jtbAnoPreA135ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA135) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA135`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A135, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA135ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA135ActionPerformed

        if (jtbAnoPreA135.isSelected()) {
            jspAnoPreA135.setVisible(true);
            jspAnoPreA135.setSize(170, 100);
        } else {
            jspAnoPreA135.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA135ActionPerformed

    /**
     * Método btnFotPreA134ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A134 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A16 (referente ao item A134)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A134, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA134ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA134ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA134ActionPerformed

    /**
     * Método jtbAnoPreA134ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA134) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA134`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A134, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA134ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA134ActionPerformed

        if (jtbAnoPreA134.isSelected()) {
            jspAnoPreA134.setVisible(true);
            jspAnoPreA134.setSize(170, 100);
        } else {
            jspAnoPreA134.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA134ActionPerformed

    /**
     * Método btnFotPreA133ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A133 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A15 (referente ao item A133)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A133, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA133ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA133ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA133ActionPerformed

    /**
     * Método jtbAnoPreA133ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA133) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA133`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A133, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA133ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA133ActionPerformed

        if (jtbAnoPreA133.isSelected()) {
            jspAnoPreA133.setVisible(true);
            jspAnoPreA133.setSize(170, 100);
        } else {
            jspAnoPreA133.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA133ActionPerformed

    /**
     * Método btnFotPreA132ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A132 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A14 (referente ao item A132)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A132, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA132ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA132ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA132ActionPerformed

    /**
     * Método jtbAnoPreA132ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA132) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA132`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A132, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA132ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA132ActionPerformed

        if (jtbAnoPreA132.isSelected()) {
            jspAnoPreA132.setVisible(true);
            jspAnoPreA132.setSize(170, 100);
        } else {
            jspAnoPreA132.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA132ActionPerformed

    /**
     * Método btnFotPreA131ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A131 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A13 (referente ao item A131)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A131, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA131ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA131ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA131ActionPerformed

    /**
     * Método jtbAnoPreA131ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA131) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA131`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A131, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA131ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA131ActionPerformed

        if (jtbAnoPreA131.isSelected()) {
            jspAnoPreA131.setVisible(true);
            jspAnoPreA131.setSize(170, 100);
        } else {
            jspAnoPreA131.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA131ActionPerformed

    /**
     * Método btnFotPreA130ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A130 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A12 (referente ao item A130)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A130, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA130ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA130ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA130ActionPerformed

    /**
     * Método jtbAnoPreA130ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA130) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA130`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A130, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA130ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA130ActionPerformed

        if (jtbAnoPreA130.isSelected()) {
            jspAnoPreA130.setVisible(true);
            jspAnoPreA130.setSize(170, 100);
        } else {
            jspAnoPreA130.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA130ActionPerformed

    /**
     * Método btnFotPreA129ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A129 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A11 (referente ao item A129)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A129, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA129ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA129ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA129ActionPerformed

    /**
     * Método jtbAnoPreA129ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA129) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA129`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A129, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA129ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA129ActionPerformed

        if (jtbAnoPreA129.isSelected()) {
            jspAnoPreA129.setVisible(true);
            jspAnoPreA129.setSize(170, 100);
        } else {
            jspAnoPreA129.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA129ActionPerformed

    /**
     * Método btnFotPreA128ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A128 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A10" (referente ao item A128)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A128, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA128ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA128ActionPerformed

    /**
     * Método jtbAnoPreA129ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA129) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA129`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A129, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA128ActionPerformed

        if (jtbAnoPreA128.isSelected()) {
            jspAnoPreA128.setVisible(true);
            jspAnoPreA128.setSize(170, 100);
        } else {
            jspAnoPreA128.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA128ActionPerformed

    /**
     * Método btnFotPreA127ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A127 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A9" (referente ao item A127)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A127, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA127ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA127ActionPerformed

    /**
     * Método jtbAnoPreA127ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA127) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA127`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A127, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA127ActionPerformed

        if (jtbAnoPreA127.isSelected()) {
            jspAnoPreA127.setVisible(true);
            jspAnoPreA127.setSize(170, 100);
        } else {
            jspAnoPreA127.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA127ActionPerformed

    /**
     * Método btnFotPreA126ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A126 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A8" (referente ao item A126)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A126, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA126ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA126ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA126ActionPerformed

    /**
     * Método jtbAnoPreA126ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA126) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA126`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A126, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA126ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA126ActionPerformed

        if (jtbAnoPreA126.isSelected()) {
            jspAnoPreA126.setVisible(true);
            jspAnoPreA126.setSize(170, 100);
        } else {
            jspAnoPreA126.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA126ActionPerformed

    /**
     * Método btnFotPreA125ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A125 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A7" (referente ao item A125)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A125, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA125ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA125ActionPerformed

    /**
     * Método jtbAnoPreA125ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA125) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA125`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A125, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA125ActionPerformed

        if (jtbAnoPreA125.isSelected()) {
            jspAnoPreA125.setVisible(true);
            jspAnoPreA125.setSize(170, 100);
        } else {
            jspAnoPreA125.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA125ActionPerformed

    /**
     * Método btnFotPreA124ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A124 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A6" (referente ao item A124)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A124, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA124ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA124ActionPerformed

    /**
     * Método jtbAnoPreA124ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA124) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA124`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A124, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA124ActionPerformed

        if (jtbAnoPreA124.isSelected()) {
            jspAnoPreA124.setVisible(true);
            jspAnoPreA124.setSize(170, 100);
        } else {
            jspAnoPreA124.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA124ActionPerformed

    /**
     * Método btnFotPreA123ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A123 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A6" (referente ao item A123)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A124, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA123ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA123ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA123ActionPerformed

    /**
     * Método jtbAnoPreA123ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA123) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA123`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A123, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA123ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA123ActionPerformed

        if (jtbAnoPreA123.isSelected()) {
            jspAnoPreA123.setVisible(true);
            jspAnoPreA123.setSize(170, 100);
        } else {
            jspAnoPreA123.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA123ActionPerformed

    /**
     * Método btnFotPreA122ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A122 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A6" (referente ao item A122)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A122, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA122ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA122ActionPerformed

    /**
     * Método jtbAnoPreA122ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA122) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA122`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A122, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA122ActionPerformed

        if (jtbAnoPreA122.isSelected()) {
            jspAnoPreA122.setVisible(true);
            jspAnoPreA122.setSize(170, 100);
        } else {
            jspAnoPreA122.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA122ActionPerformed

    /**
     * Método btnFotPreA121ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A121 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A3" (referente ao item A121)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A121, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA121ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA121ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA121ActionPerformed

    /**
     * Método jtbAnoPreA121ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA121) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA121`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A121, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA121ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA121ActionPerformed

        if (jtbAnoPreA121.isSelected()) {
            jspAnoPreA121.setVisible(true);
            jspAnoPreA121.setSize(170, 100);
        } else {
            jspAnoPreA121.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA121ActionPerformed

    /**
     * Método btnFotPreA120ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A120 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A2" (referente ao item A120)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A120, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA120ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA120ActionPerformed

    /**
     * Método jtbAnoPreA120ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA120) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA120`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A120, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA120ActionPerformed

        if (jtbAnoPreA120.isSelected()) {
            jspAnoPreA120.setVisible(true);
            jspAnoPreA120.setSize(170, 100);
        } else {
            jspAnoPreA120.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA120ActionPerformed

    /**
     * Método btnFotPreA119ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A119 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A1" (referente ao item A119)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A119, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA119ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA119ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre3();
    }//GEN-LAST:event_btnFotPreA119ActionPerformed

    /**
     * Método jtbAnoPreA119ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA119) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA119`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A119, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA119ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA119ActionPerformed

        if (jtbAnoPreA119.isSelected()) {
            jspAnoPreA119.setVisible(true);
            jspAnoPreA119.setSize(170, 100);
        } else {
            jspAnoPreA119.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA119ActionPerformed

    /**
     * Método btnFotPreA235ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A235 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A33" (referente ao item A235)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A235, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA235ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA235ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A33";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA235ActionPerformed

    /**
     * Método jtbAnoPreA235ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA235) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA235`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A235, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA235ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA235ActionPerformed

        if (jtbAnoPreA235.isSelected()) {
            jspAnoPreA235.setVisible(true);
            jspAnoPreA235.setSize(170, 100);
        } else {
            jspAnoPreA235.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA235ActionPerformed

    /**
     * Método btnFotPreA234ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A234 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A32" (referente ao item A234)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A233, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA234ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA234ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A32";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA234ActionPerformed

    /**
     * Método jtbAnoPreA234ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA234) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA234`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A234, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA234ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA234ActionPerformed

        if (jtbAnoPreA234.isSelected()) {
            jspAnoPreA234.setVisible(true);
            jspAnoPreA234.setSize(170, 100);
        } else {
            jspAnoPreA234.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA234ActionPerformed

    /**
     * Método btnFotPreA233ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A233 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A31" (referente ao item A233)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A233, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA233ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA233ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A31";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA233ActionPerformed

    /**
     * Método jtbAnoPreA233ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA233) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA233`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A233, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA233ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA233ActionPerformed

        if (jtbAnoPreA233.isSelected()) {
            jspAnoPreA233.setVisible(true);
            jspAnoPreA233.setSize(170, 100);
        } else {
            jspAnoPreA233.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA233ActionPerformed

    /**
     * Método btnFotPreA232ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A232 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A30" (referente ao item A232)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A232, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA232ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA232ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A30";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA232ActionPerformed

    /**
     * Método jtbAnoPreA232ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA232) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA232`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A232, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA232ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA232ActionPerformed

        if (jtbAnoPreA232.isSelected()) {
            jspAnoPreA232.setVisible(true);
            jspAnoPreA232.setSize(170, 100);
        } else {
            jspAnoPreA232.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA232ActionPerformed

    /**
     * Método btnFotPreA231ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A231 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A29" (referente ao item A231)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A231, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA231ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA231ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A29";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA231ActionPerformed

    /**
     * Método jtbAnoPreA231ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA231) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA231`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A231, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA231ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA231ActionPerformed

        if (jtbAnoPreA231.isSelected()) {
            jspAnoPreA231.setVisible(true);
            jspAnoPreA231.setSize(170, 100);
        } else {
            jspAnoPreA231.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA231ActionPerformed

    /**
     * Método btnFotPreA230ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A230 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A28" (referente ao item A230)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A230, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA230ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA230ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA230ActionPerformed

    /**
     * Método jtbAnoPreA230ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA230) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA230`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A230, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA230ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA230ActionPerformed

        if (jtbAnoPreA230.isSelected()) {
            jspAnoPreA230.setVisible(true);
            jspAnoPreA230.setSize(170, 100);
        } else {
            jspAnoPreA230.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA230ActionPerformed

    /**
     * Método btnFotPreA229ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A229 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A27" (referente ao item A229)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A229, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA229ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA229ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A27";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA229ActionPerformed

    /**
     * Método jtbAnoPreA229ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA229) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA229`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A229, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA229ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA229ActionPerformed

        if (jtbAnoPreA229.isSelected()) {
            jspAnoPreA229.setVisible(true);
            jspAnoPreA229.setSize(170, 100);
        } else {
            jspAnoPreA229.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA229ActionPerformed

    /**
     * Método btnFotPreA228ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A228 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A26" (referente ao item A228)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A228, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA228ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA228ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA228ActionPerformed

    /**
     * Método jtbAnoPreA228ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA228) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA228`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A228, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA228ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA228ActionPerformed

        if (jtbAnoPreA228.isSelected()) {
            jspAnoPreA228.setVisible(true);
            jspAnoPreA228.setSize(170, 100);
        } else {
            jspAnoPreA228.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA228ActionPerformed

    /**
     * Método btnFotPreA227ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A227 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A25" (referente ao item A227)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A227, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA227ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA227ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA227ActionPerformed

    /**
     * Método jtbAnoPreA227ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA227) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA227`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A227, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA227ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA227ActionPerformed

        if (jtbAnoPreA227.isSelected()) {
            jspAnoPreA227.setVisible(true);
            jspAnoPreA227.setSize(170, 100);
        } else {
            jspAnoPreA227.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA227ActionPerformed

    /**
     * Método btnFotPreA226ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A226 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A24" (referente ao item A226)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A226, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA226ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA226ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA226ActionPerformed

    /**
     * Método jtbAnoPreA226ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA226) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA226`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A226, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA226ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA226ActionPerformed

        if (jtbAnoPreA226.isSelected()) {
            jspAnoPreA226.setVisible(true);
            jspAnoPreA226.setSize(170, 100);
        } else {
            jspAnoPreA226.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA226ActionPerformed

    /**
     * Método btnFotPreA225ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A225 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A23" (referente ao item A225)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A225, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA225ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA225ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA225ActionPerformed

    /**
     * Método jtbAnoPreA225ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA225) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA225`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A225, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA225ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA225ActionPerformed

        if (jtbAnoPreA225.isSelected()) {
            jspAnoPreA225.setVisible(true);
            jspAnoPreA225.setSize(170, 100);
        } else {
            jspAnoPreA225.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA225ActionPerformed

    /**
     * Método btnFotPreA224ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A224 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A22" (referente ao item A224)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A224, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA224ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA224ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA224ActionPerformed

    /**
     * Método jtbAnoPreA224ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA224) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA224`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A224, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA224ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA224ActionPerformed

        if (jtbAnoPreA224.isSelected()) {
            jspAnoPreA224.setVisible(true);
            jspAnoPreA224.setSize(170, 100);
        } else {
            jspAnoPreA224.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA224ActionPerformed

    /**
     * Método btnFotPreA223ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A223 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A21" (referente ao item A223)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A223, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA223ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA223ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA223ActionPerformed

    /**
     * Método jtbAnoPreA223ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA223) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA223`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A223, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA223ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA223ActionPerformed

        if (jtbAnoPreA223.isSelected()) {
            jspAnoPreA223.setVisible(true);
            jspAnoPreA223.setSize(170, 100);
        } else {
            jspAnoPreA223.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA223ActionPerformed

    /**
     * Método btnFotPreA222ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A222 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A20" (referente ao item A222)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A222, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA222ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA222ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA222ActionPerformed

    /**
     * Método jtbAnoPreA222ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA222) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA222`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A222, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA222ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA222ActionPerformed

        if (jtbAnoPreA222.isSelected()) {
            jspAnoPreA222.setVisible(true);
            jspAnoPreA222.setSize(170, 100);
        } else {
            jspAnoPreA222.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA222ActionPerformed

    /**
     * Método btnFotPreA221ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A221 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A19" (referente ao item A221)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A221, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA221ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA221ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA221ActionPerformed

    /**
     * Método jtbAnoPreA221ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA221) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA221`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A221, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA221ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA221ActionPerformed

        if (jtbAnoPreA221.isSelected()) {
            jspAnoPreA221.setVisible(true);
            jspAnoPreA221.setSize(170, 100);
        } else {
            jspAnoPreA221.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA221ActionPerformed

    /**
     * Método btnFotPreA220ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A220 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A18" (referente ao item A220)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A220, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA220ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA220ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA220ActionPerformed

    /**
     * Método jtbAnoPreA220ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA220) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA220`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A220, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA220ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA220ActionPerformed

        if (jtbAnoPreA220.isSelected()) {
            jspAnoPreA220.setVisible(true);
            jspAnoPreA220.setSize(170, 100);
        } else {
            jspAnoPreA220.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA220ActionPerformed

    /**
     * Método btnFotPreA219ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A219 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A17" (referente ao item A219)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A219, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA219ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA219ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA219ActionPerformed

    /**
     * Método jtbAnoPreA219ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA219) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA219`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A219, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA219ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA219ActionPerformed

        if (jtbAnoPreA219.isSelected()) {
            jspAnoPreA219.setVisible(true);
            jspAnoPreA219.setSize(170, 100);
        } else {
            jspAnoPreA219.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA219ActionPerformed

    /**
     * Método btnFotPreA218ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A218 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A16" (referente ao item A218)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A218, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA218ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA218ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA218ActionPerformed

    /**
     * Método jtbAnoPreA218ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA218) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA218`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A218, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA218ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA218ActionPerformed

        if (jtbAnoPreA218.isSelected()) {
            jspAnoPreA218.setVisible(true);
            jspAnoPreA218.setSize(170, 100);
        } else {
            jspAnoPreA218.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA218ActionPerformed

    /**
     * Método btnFotPreA217ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A217 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A15" (referente ao item A217)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A217, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA217ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA217ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA217ActionPerformed

    /**
     * Método jtbAnoPreA217ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA217) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA217`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A217, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA217ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA217ActionPerformed

        if (jtbAnoPreA217.isSelected()) {
            jspAnoPreA217.setVisible(true);
            jspAnoPreA217.setSize(170, 100);
        } else {
            jspAnoPreA217.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA217ActionPerformed

    /**
     * Método btnFotPreA216ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A216 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A14" (referente ao item A216)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A216, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA216ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA216ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA216ActionPerformed

    /**
     * Método jtbAnoPreA216ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA216) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA216`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A216, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA216ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA216ActionPerformed

        if (jtbAnoPreA216.isSelected()) {
            jspAnoPreA216.setVisible(true);
            jspAnoPreA216.setSize(170, 100);
        } else {
            jspAnoPreA216.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA216ActionPerformed

    /**
     * Método btnFotPreA215ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A215 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A13" (referente ao item A215)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A215, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA215ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA215ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA215ActionPerformed

    /**
     * Método jtbAnoPreA215ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA215) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA215`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A215, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA215ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA215ActionPerformed

        if (jtbAnoPreA215.isSelected()) {
            jspAnoPreA215.setVisible(true);
            jspAnoPreA215.setSize(170, 100);
        } else {
            jspAnoPreA215.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA215ActionPerformed

    /**
     * Método btnFotPreA214ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A214 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A12" (referente ao item A214)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A214, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA214ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA214ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA214ActionPerformed

    /**
     * Método jtbAnoPreA214ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA214) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA214`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A214, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA214ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA214ActionPerformed

        if (jtbAnoPreA214.isSelected()) {
            jspAnoPreA214.setVisible(true);
            jspAnoPreA214.setSize(170, 100);
        } else {
            jspAnoPreA214.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA214ActionPerformed

    /**
     * Método btnFotPreA213ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A213 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A11" (referente ao item A213)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A213, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA213ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA213ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA213ActionPerformed

    /**
     * Método jtbAnoPreA213ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA213) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA213`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A213, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA213ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA213ActionPerformed

        if (jtbAnoPreA213.isSelected()) {
            jspAnoPreA213.setVisible(true);
            jspAnoPreA213.setSize(170, 100);
        } else {
            jspAnoPreA213.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA213ActionPerformed

    /**
     * Método btnFotPreA212ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A212 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A10" (referente ao item A212)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A212, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA212ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA212ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA212ActionPerformed

    /**
     * Método jtbAnoPreA212ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA212) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA212`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A212, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA212ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA212ActionPerformed

        if (jtbAnoPreA212.isSelected()) {
            jspAnoPreA212.setVisible(true);
            jspAnoPreA212.setSize(170, 100);
        } else {
            jspAnoPreA212.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA212ActionPerformed

    /**
     * Método btnFotPreA211ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A211 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A9" (referente ao item A211)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A211, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA211ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA211ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA211ActionPerformed

    /**
     * Método jtbAnoPreA211ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA211) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA211`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A211, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA211ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA211ActionPerformed

        if (jtbAnoPreA211.isSelected()) {
            jspAnoPreA211.setVisible(true);
            jspAnoPreA211.setSize(170, 100);
        } else {
            jspAnoPreA211.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA211ActionPerformed

    /**
     * Método btnFotPreA210ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A210 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A8" (referente ao item A210)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A210, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA210ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA210ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA210ActionPerformed

    /**
     * Método jtbAnoPreA210ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA210) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA210`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A210, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA210ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA210ActionPerformed

        if (jtbAnoPreA210.isSelected()) {
            jspAnoPreA210.setVisible(true);
            jspAnoPreA210.setSize(170, 100);
        } else {
            jspAnoPreA210.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA210ActionPerformed

    /**
     * Método btnFotPreA209ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A209 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A7" (referente ao item A209)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A209, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA209ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA209ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA209ActionPerformed

    /**
     * Método jtbAnoPreA209ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA209) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA209`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A209, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA209ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA209ActionPerformed

        if (jtbAnoPreA209.isSelected()) {
            jspAnoPreA209.setVisible(true);
            jspAnoPreA209.setSize(170, 100);
        } else {
            jspAnoPreA209.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA209ActionPerformed

    /**
     * Método btnFotPreA208ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A208 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A6" (referente ao item A208)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A208, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA208ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA208ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA208ActionPerformed

    /**
     * Método jtbAnoPreA208ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA208) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA208`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A208, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA208ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA208ActionPerformed

        if (jtbAnoPreA208.isSelected()) {
            jspAnoPreA208.setVisible(true);
            jspAnoPreA208.setSize(170, 100);
        } else {
            jspAnoPreA208.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA208ActionPerformed

    /**
     * Método btnFotPreA207ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A207 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A5" (referente ao item A207)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A207, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA207ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA207ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA207ActionPerformed

    /**
     * Método jtbAnoPreA207ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA207) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA207`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A207, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA207ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA207ActionPerformed

        if (jtbAnoPreA207.isSelected()) {
            jspAnoPreA207.setVisible(true);
            jspAnoPreA207.setSize(170, 100);
        } else {
            jspAnoPreA207.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA207ActionPerformed

    /**
     * Método btnFotPreA206ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A206 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A4" (referente ao item A206)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A206, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA206ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA206ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA206ActionPerformed

    /**
     * Método jtbAnoPreA206ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA206) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA206`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A206, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA206ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA206ActionPerformed

        if (jtbAnoPreA206.isSelected()) {
            jspAnoPreA206.setVisible(true);
            jspAnoPreA206.setSize(170, 100);
        } else {
            jspAnoPreA206.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA206ActionPerformed

    /**
     * Método btnFotPreA205ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A205 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A3" (referente ao item A205)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A205, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA205ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA205ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA205ActionPerformed

    /**
     * Método jtbAnoPreA205ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA205) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA205`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A205, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA205ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA205ActionPerformed

        if (jtbAnoPreA205.isSelected()) {
            jspAnoPreA205.setVisible(true);
            jspAnoPreA205.setSize(170, 100);
        } else {
            jspAnoPreA205.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA205ActionPerformed

    /**
     * Método btnFotPreA204ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A204 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A2" (referente ao item A204)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A204, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA204ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA204ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA204ActionPerformed

    /**
     * Método jtbAnoPreA204ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA204) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA204`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A204, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA204ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA204ActionPerformed

        if (jtbAnoPreA204.isSelected()) {
            jspAnoPreA204.setVisible(true);
            jspAnoPreA204.setSize(170, 100);
        } else {
            jspAnoPreA204.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA204ActionPerformed

    /**
     * Método btnFotPreA203ActionPerformed
     *
     * Acionado quando o botão relacionado ao item A203 reventiva hidráulica
     * trimestral é clicado.
     *
     * Funcionalidades: - Limpa os campos e componentes visuais relacionados à
     * visualização de fotos: - Remove ícone da imagem (`lblFotEltMen8`). -
     * Limpa o campo de nome do arquivo (`txtFotPre8`) e o campo de pesquisa
     * (`txtPesArq`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Define
     * o valor da variável `arq` como `"A1" (referente ao item A203)- Torna o
     * painel de arquivos (`jpaArqPre`) visível e ajusta seu tamanho. - Chama o
     * método `arq_pre4()` para carregar os arquivos da preventiva trimestral.
     *
     * Esse método prepara a interface para exibir os arquivos relacionados ao
     * item A203, garantindo uma experiência limpa e focada para o usuário.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnFotPreA203ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA203ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre5();
    }//GEN-LAST:event_btnFotPreA203ActionPerformed

    /**
     * Método jtbAnoPreA203ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA203) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado: - Se sim, torna
     * o campo de observação (`jspAnoPreA203`) visível e ajusta seu tamanho. -
     * Se não, oculta o campo de observação.
     *
     * Esse método permite ao usuário expandir ou ocultar a área de observação
     * do item A203, tornando a interface mais dinâmica e adaptável.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA203ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA203ActionPerformed

        if (jtbAnoPreA203.isSelected()) {
            jspAnoPreA203.setVisible(true);
            jspAnoPreA203.setSize(170, 100);
        } else {
            jspAnoPreA203.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA203ActionPerformed

    /**
     * Método btnImpPre4ActionPerformed
     *
     * Acionado quando o botão de impressão da preventiva elétrica mensal é
     * clicado.
     *
     * Funcionalidades: - Define a variável `prev` como `"mensal"`, indicando o
     * tipo de preventiva a ser impressa. - Chama o método `imprimir()` para
     * iniciar o processo de geração e envio da impressão.
     *
     * Essa abordagem permite que o método `imprimir()` seja reutilizado para
     * diferentes tipos de preventiva, bastando ajustar o valor da variável
     * `prev` conforme o botão acionado.
     *
     * @param evt o evento gerado pelo clique no botão de impressão
     */
    private void btnImpPre3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPre3ActionPerformed

        prev = "mensal";
        imprimir();
    }//GEN-LAST:event_btnImpPre3ActionPerformed

    /**
     * Método btnImpPre4ActionPerformed
     *
     * Acionado quando o botão de impressão da preventiva elétrica semestral é
     * clicado.
     *
     * Funcionalidades: - Define a variável `prev` como `"semestral"`, indicando
     * o tipo de preventiva a ser impressa. - Chama o método `imprimir()` para
     * iniciar o processo de geração e envio da impressão.
     *
     * Essa abordagem permite que o método `imprimir()` seja reutilizado para
     * diferentes tipos de preventiva, bastando ajustar o valor da variável
     * `prev` conforme o botão acionado.
     *
     * @param evt o evento gerado pelo clique no botão de impressão
     */
    private void btnImpPre4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPre4ActionPerformed

        prev = "semestral";
        imprimir();
    }//GEN-LAST:event_btnImpPre4ActionPerformed

    /**
     * Método btnImpPre5ActionPerformed
     *
     * Acionado quando o botão de impressão da preventiva elétrica trimestral é
     * clicado.
     *
     * Funcionalidades: - Define a variável `prev` como `"trimestral"`,
     * indicando o tipo de preventiva a ser impressa. - Chama o método
     * `imprimir()` para iniciar o processo de geração e envio da impressão.
     *
     * Essa abordagem permite que o método `imprimir()` seja reutilizado para
     * diferentes tipos de preventiva, bastando ajustar o valor da variável
     * `prev` conforme o botão acionado.
     *
     * @param evt o evento gerado pelo clique no botão de impressão
     */
    private void btnImpPre5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPre5ActionPerformed

        prev = "trimestral";
        imprimir();
    }//GEN-LAST:event_btnImpPre5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFotPreA119;
    private javax.swing.JButton btnFotPreA120;
    private javax.swing.JButton btnFotPreA121;
    private javax.swing.JButton btnFotPreA122;
    private javax.swing.JButton btnFotPreA123;
    private javax.swing.JButton btnFotPreA124;
    private javax.swing.JButton btnFotPreA125;
    private javax.swing.JButton btnFotPreA126;
    private javax.swing.JButton btnFotPreA127;
    private javax.swing.JButton btnFotPreA128;
    private javax.swing.JButton btnFotPreA129;
    private javax.swing.JButton btnFotPreA130;
    private javax.swing.JButton btnFotPreA131;
    private javax.swing.JButton btnFotPreA132;
    private javax.swing.JButton btnFotPreA133;
    private javax.swing.JButton btnFotPreA134;
    private javax.swing.JButton btnFotPreA135;
    private javax.swing.JButton btnFotPreA136;
    private javax.swing.JButton btnFotPreA137;
    private javax.swing.JButton btnFotPreA138;
    private javax.swing.JButton btnFotPreA139;
    private javax.swing.JButton btnFotPreA140;
    private javax.swing.JButton btnFotPreA141;
    private javax.swing.JButton btnFotPreA142;
    private javax.swing.JButton btnFotPreA143;
    private javax.swing.JButton btnFotPreA144;
    private javax.swing.JButton btnFotPreA145;
    private javax.swing.JButton btnFotPreA173;
    private javax.swing.JButton btnFotPreA174;
    private javax.swing.JButton btnFotPreA175;
    private javax.swing.JButton btnFotPreA176;
    private javax.swing.JButton btnFotPreA177;
    private javax.swing.JButton btnFotPreA178;
    private javax.swing.JButton btnFotPreA179;
    private javax.swing.JButton btnFotPreA180;
    private javax.swing.JButton btnFotPreA181;
    private javax.swing.JButton btnFotPreA182;
    private javax.swing.JButton btnFotPreA183;
    private javax.swing.JButton btnFotPreA184;
    private javax.swing.JButton btnFotPreA185;
    private javax.swing.JButton btnFotPreA186;
    private javax.swing.JButton btnFotPreA187;
    private javax.swing.JButton btnFotPreA188;
    private javax.swing.JButton btnFotPreA189;
    private javax.swing.JButton btnFotPreA190;
    private javax.swing.JButton btnFotPreA191;
    private javax.swing.JButton btnFotPreA192;
    private javax.swing.JButton btnFotPreA193;
    private javax.swing.JButton btnFotPreA194;
    private javax.swing.JButton btnFotPreA195;
    private javax.swing.JButton btnFotPreA196;
    private javax.swing.JButton btnFotPreA197;
    private javax.swing.JButton btnFotPreA198;
    private javax.swing.JButton btnFotPreA199;
    private javax.swing.JButton btnFotPreA200;
    private javax.swing.JButton btnFotPreA201;
    private javax.swing.JButton btnFotPreA202;
    private javax.swing.JButton btnFotPreA203;
    private javax.swing.JButton btnFotPreA204;
    private javax.swing.JButton btnFotPreA205;
    private javax.swing.JButton btnFotPreA206;
    private javax.swing.JButton btnFotPreA207;
    private javax.swing.JButton btnFotPreA208;
    private javax.swing.JButton btnFotPreA209;
    private javax.swing.JButton btnFotPreA210;
    private javax.swing.JButton btnFotPreA211;
    private javax.swing.JButton btnFotPreA212;
    private javax.swing.JButton btnFotPreA213;
    private javax.swing.JButton btnFotPreA214;
    private javax.swing.JButton btnFotPreA215;
    private javax.swing.JButton btnFotPreA216;
    private javax.swing.JButton btnFotPreA217;
    private javax.swing.JButton btnFotPreA218;
    private javax.swing.JButton btnFotPreA219;
    private javax.swing.JButton btnFotPreA220;
    private javax.swing.JButton btnFotPreA221;
    private javax.swing.JButton btnFotPreA222;
    private javax.swing.JButton btnFotPreA223;
    private javax.swing.JButton btnFotPreA224;
    private javax.swing.JButton btnFotPreA225;
    private javax.swing.JButton btnFotPreA226;
    private javax.swing.JButton btnFotPreA227;
    private javax.swing.JButton btnFotPreA228;
    private javax.swing.JButton btnFotPreA229;
    private javax.swing.JButton btnFotPreA230;
    private javax.swing.JButton btnFotPreA231;
    private javax.swing.JButton btnFotPreA232;
    private javax.swing.JButton btnFotPreA233;
    private javax.swing.JButton btnFotPreA234;
    private javax.swing.JButton btnFotPreA235;
    private javax.swing.JButton btnImpPre3;
    private javax.swing.JButton btnImpPre4;
    private javax.swing.JButton btnImpPre5;
    public static javax.swing.JButton btnSalFotPre8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel jpaAba4;
    private javax.swing.JPanel jpaAba5;
    private javax.swing.JPanel jpaAba6;
    public static javax.swing.JPanel jpaArqPre;
    private javax.swing.JScrollPane jspAnoPreA119;
    private javax.swing.JScrollPane jspAnoPreA120;
    private javax.swing.JScrollPane jspAnoPreA121;
    private javax.swing.JScrollPane jspAnoPreA122;
    private javax.swing.JScrollPane jspAnoPreA123;
    private javax.swing.JScrollPane jspAnoPreA124;
    private javax.swing.JScrollPane jspAnoPreA125;
    private javax.swing.JScrollPane jspAnoPreA126;
    private javax.swing.JScrollPane jspAnoPreA127;
    private javax.swing.JScrollPane jspAnoPreA128;
    private javax.swing.JScrollPane jspAnoPreA129;
    private javax.swing.JScrollPane jspAnoPreA130;
    private javax.swing.JScrollPane jspAnoPreA131;
    private javax.swing.JScrollPane jspAnoPreA132;
    private javax.swing.JScrollPane jspAnoPreA133;
    private javax.swing.JScrollPane jspAnoPreA134;
    private javax.swing.JScrollPane jspAnoPreA135;
    private javax.swing.JScrollPane jspAnoPreA136;
    private javax.swing.JScrollPane jspAnoPreA137;
    private javax.swing.JScrollPane jspAnoPreA138;
    private javax.swing.JScrollPane jspAnoPreA139;
    private javax.swing.JScrollPane jspAnoPreA140;
    private javax.swing.JScrollPane jspAnoPreA141;
    private javax.swing.JScrollPane jspAnoPreA142;
    private javax.swing.JScrollPane jspAnoPreA143;
    private javax.swing.JScrollPane jspAnoPreA144;
    private javax.swing.JScrollPane jspAnoPreA145;
    private javax.swing.JScrollPane jspAnoPreA173;
    private javax.swing.JScrollPane jspAnoPreA174;
    private javax.swing.JScrollPane jspAnoPreA175;
    private javax.swing.JScrollPane jspAnoPreA176;
    private javax.swing.JScrollPane jspAnoPreA177;
    private javax.swing.JScrollPane jspAnoPreA178;
    private javax.swing.JScrollPane jspAnoPreA179;
    private javax.swing.JScrollPane jspAnoPreA180;
    private javax.swing.JScrollPane jspAnoPreA181;
    private javax.swing.JScrollPane jspAnoPreA182;
    private javax.swing.JScrollPane jspAnoPreA183;
    private javax.swing.JScrollPane jspAnoPreA184;
    private javax.swing.JScrollPane jspAnoPreA185;
    private javax.swing.JScrollPane jspAnoPreA186;
    private javax.swing.JScrollPane jspAnoPreA187;
    private javax.swing.JScrollPane jspAnoPreA188;
    private javax.swing.JScrollPane jspAnoPreA189;
    private javax.swing.JScrollPane jspAnoPreA190;
    private javax.swing.JScrollPane jspAnoPreA191;
    private javax.swing.JScrollPane jspAnoPreA192;
    private javax.swing.JScrollPane jspAnoPreA193;
    private javax.swing.JScrollPane jspAnoPreA194;
    private javax.swing.JScrollPane jspAnoPreA195;
    private javax.swing.JScrollPane jspAnoPreA196;
    private javax.swing.JScrollPane jspAnoPreA197;
    private javax.swing.JScrollPane jspAnoPreA198;
    private javax.swing.JScrollPane jspAnoPreA199;
    private javax.swing.JScrollPane jspAnoPreA200;
    private javax.swing.JScrollPane jspAnoPreA201;
    private javax.swing.JScrollPane jspAnoPreA202;
    private javax.swing.JScrollPane jspAnoPreA203;
    private javax.swing.JScrollPane jspAnoPreA204;
    private javax.swing.JScrollPane jspAnoPreA205;
    private javax.swing.JScrollPane jspAnoPreA206;
    private javax.swing.JScrollPane jspAnoPreA207;
    private javax.swing.JScrollPane jspAnoPreA208;
    private javax.swing.JScrollPane jspAnoPreA209;
    private javax.swing.JScrollPane jspAnoPreA210;
    private javax.swing.JScrollPane jspAnoPreA211;
    private javax.swing.JScrollPane jspAnoPreA212;
    private javax.swing.JScrollPane jspAnoPreA213;
    private javax.swing.JScrollPane jspAnoPreA214;
    private javax.swing.JScrollPane jspAnoPreA215;
    private javax.swing.JScrollPane jspAnoPreA216;
    private javax.swing.JScrollPane jspAnoPreA217;
    private javax.swing.JScrollPane jspAnoPreA218;
    private javax.swing.JScrollPane jspAnoPreA219;
    private javax.swing.JScrollPane jspAnoPreA220;
    private javax.swing.JScrollPane jspAnoPreA221;
    private javax.swing.JScrollPane jspAnoPreA222;
    private javax.swing.JScrollPane jspAnoPreA223;
    private javax.swing.JScrollPane jspAnoPreA224;
    private javax.swing.JScrollPane jspAnoPreA225;
    private javax.swing.JScrollPane jspAnoPreA226;
    private javax.swing.JScrollPane jspAnoPreA227;
    private javax.swing.JScrollPane jspAnoPreA228;
    private javax.swing.JScrollPane jspAnoPreA229;
    private javax.swing.JScrollPane jspAnoPreA230;
    private javax.swing.JScrollPane jspAnoPreA231;
    private javax.swing.JScrollPane jspAnoPreA232;
    private javax.swing.JScrollPane jspAnoPreA233;
    private javax.swing.JScrollPane jspAnoPreA234;
    private javax.swing.JScrollPane jspAnoPreA235;
    private javax.swing.JTextArea jtaAnoA119;
    private javax.swing.JTextArea jtaAnoA120;
    private javax.swing.JTextArea jtaAnoA121;
    private javax.swing.JTextArea jtaAnoA122;
    private javax.swing.JTextArea jtaAnoA123;
    private javax.swing.JTextArea jtaAnoA124;
    private javax.swing.JTextArea jtaAnoA125;
    private javax.swing.JTextArea jtaAnoA126;
    private javax.swing.JTextArea jtaAnoA127;
    private javax.swing.JTextArea jtaAnoA128;
    private javax.swing.JTextArea jtaAnoA129;
    private javax.swing.JTextArea jtaAnoA130;
    private javax.swing.JTextArea jtaAnoA131;
    private javax.swing.JTextArea jtaAnoA132;
    private javax.swing.JTextArea jtaAnoA133;
    private javax.swing.JTextArea jtaAnoA134;
    private javax.swing.JTextArea jtaAnoA135;
    private javax.swing.JTextArea jtaAnoA136;
    private javax.swing.JTextArea jtaAnoA137;
    private javax.swing.JTextArea jtaAnoA138;
    private javax.swing.JTextArea jtaAnoA139;
    private javax.swing.JTextArea jtaAnoA140;
    private javax.swing.JTextArea jtaAnoA141;
    private javax.swing.JTextArea jtaAnoA142;
    private javax.swing.JTextArea jtaAnoA143;
    private javax.swing.JTextArea jtaAnoA144;
    private javax.swing.JTextArea jtaAnoA145;
    private javax.swing.JTextArea jtaAnoA173;
    private javax.swing.JTextArea jtaAnoA174;
    private javax.swing.JTextArea jtaAnoA175;
    private javax.swing.JTextArea jtaAnoA176;
    private javax.swing.JTextArea jtaAnoA177;
    private javax.swing.JTextArea jtaAnoA178;
    private javax.swing.JTextArea jtaAnoA179;
    private javax.swing.JTextArea jtaAnoA180;
    private javax.swing.JTextArea jtaAnoA181;
    private javax.swing.JTextArea jtaAnoA182;
    private javax.swing.JTextArea jtaAnoA183;
    private javax.swing.JTextArea jtaAnoA184;
    private javax.swing.JTextArea jtaAnoA185;
    private javax.swing.JTextArea jtaAnoA186;
    private javax.swing.JTextArea jtaAnoA187;
    private javax.swing.JTextArea jtaAnoA188;
    private javax.swing.JTextArea jtaAnoA189;
    private javax.swing.JTextArea jtaAnoA190;
    private javax.swing.JTextArea jtaAnoA191;
    private javax.swing.JTextArea jtaAnoA192;
    private javax.swing.JTextArea jtaAnoA193;
    private javax.swing.JTextArea jtaAnoA194;
    private javax.swing.JTextArea jtaAnoA195;
    private javax.swing.JTextArea jtaAnoA196;
    private javax.swing.JTextArea jtaAnoA197;
    private javax.swing.JTextArea jtaAnoA198;
    private javax.swing.JTextArea jtaAnoA199;
    private javax.swing.JTextArea jtaAnoA200;
    private javax.swing.JTextArea jtaAnoA201;
    private javax.swing.JTextArea jtaAnoA202;
    private javax.swing.JTextArea jtaAnoA203;
    private javax.swing.JTextArea jtaAnoA204;
    private javax.swing.JTextArea jtaAnoA205;
    private javax.swing.JTextArea jtaAnoA206;
    private javax.swing.JTextArea jtaAnoA207;
    private javax.swing.JTextArea jtaAnoA208;
    private javax.swing.JTextArea jtaAnoA209;
    private javax.swing.JTextArea jtaAnoA210;
    private javax.swing.JTextArea jtaAnoA211;
    private javax.swing.JTextArea jtaAnoA212;
    private javax.swing.JTextArea jtaAnoA213;
    private javax.swing.JTextArea jtaAnoA214;
    private javax.swing.JTextArea jtaAnoA215;
    private javax.swing.JTextArea jtaAnoA216;
    private javax.swing.JTextArea jtaAnoA217;
    private javax.swing.JTextArea jtaAnoA218;
    private javax.swing.JTextArea jtaAnoA219;
    private javax.swing.JTextArea jtaAnoA220;
    private javax.swing.JTextArea jtaAnoA221;
    private javax.swing.JTextArea jtaAnoA222;
    private javax.swing.JTextArea jtaAnoA223;
    private javax.swing.JTextArea jtaAnoA224;
    private javax.swing.JTextArea jtaAnoA225;
    private javax.swing.JTextArea jtaAnoA226;
    private javax.swing.JTextArea jtaAnoA227;
    private javax.swing.JTextArea jtaAnoA228;
    private javax.swing.JTextArea jtaAnoA229;
    private javax.swing.JTextArea jtaAnoA230;
    private javax.swing.JTextArea jtaAnoA231;
    private javax.swing.JTextArea jtaAnoA232;
    private javax.swing.JTextArea jtaAnoA233;
    private javax.swing.JTextArea jtaAnoA234;
    private javax.swing.JTextArea jtaAnoA235;
    private javax.swing.JTextArea jtaEquPre3;
    private javax.swing.JTextArea jtaEquPre5;
    private javax.swing.JTextArea jtaEquPre6;
    private javax.swing.JTextArea jtaSetPre3;
    private javax.swing.JTextArea jtaSetPre5;
    private javax.swing.JTextArea jtaSetPre6;
    private javax.swing.JToggleButton jtbAnoPreA119;
    private javax.swing.JToggleButton jtbAnoPreA120;
    private javax.swing.JToggleButton jtbAnoPreA121;
    private javax.swing.JToggleButton jtbAnoPreA122;
    private javax.swing.JToggleButton jtbAnoPreA123;
    private javax.swing.JToggleButton jtbAnoPreA124;
    private javax.swing.JToggleButton jtbAnoPreA125;
    private javax.swing.JToggleButton jtbAnoPreA126;
    private javax.swing.JToggleButton jtbAnoPreA127;
    private javax.swing.JToggleButton jtbAnoPreA128;
    private javax.swing.JToggleButton jtbAnoPreA129;
    private javax.swing.JToggleButton jtbAnoPreA130;
    private javax.swing.JToggleButton jtbAnoPreA131;
    private javax.swing.JToggleButton jtbAnoPreA132;
    private javax.swing.JToggleButton jtbAnoPreA133;
    private javax.swing.JToggleButton jtbAnoPreA134;
    private javax.swing.JToggleButton jtbAnoPreA135;
    private javax.swing.JToggleButton jtbAnoPreA136;
    private javax.swing.JToggleButton jtbAnoPreA137;
    private javax.swing.JToggleButton jtbAnoPreA138;
    private javax.swing.JToggleButton jtbAnoPreA139;
    private javax.swing.JToggleButton jtbAnoPreA140;
    private javax.swing.JToggleButton jtbAnoPreA141;
    private javax.swing.JToggleButton jtbAnoPreA142;
    private javax.swing.JToggleButton jtbAnoPreA143;
    private javax.swing.JToggleButton jtbAnoPreA144;
    private javax.swing.JToggleButton jtbAnoPreA145;
    private javax.swing.JToggleButton jtbAnoPreA173;
    private javax.swing.JToggleButton jtbAnoPreA174;
    private javax.swing.JToggleButton jtbAnoPreA175;
    private javax.swing.JToggleButton jtbAnoPreA176;
    private javax.swing.JToggleButton jtbAnoPreA177;
    private javax.swing.JToggleButton jtbAnoPreA178;
    private javax.swing.JToggleButton jtbAnoPreA179;
    private javax.swing.JToggleButton jtbAnoPreA180;
    private javax.swing.JToggleButton jtbAnoPreA181;
    private javax.swing.JToggleButton jtbAnoPreA182;
    private javax.swing.JToggleButton jtbAnoPreA183;
    private javax.swing.JToggleButton jtbAnoPreA184;
    private javax.swing.JToggleButton jtbAnoPreA185;
    private javax.swing.JToggleButton jtbAnoPreA186;
    private javax.swing.JToggleButton jtbAnoPreA187;
    private javax.swing.JToggleButton jtbAnoPreA188;
    private javax.swing.JToggleButton jtbAnoPreA189;
    private javax.swing.JToggleButton jtbAnoPreA190;
    private javax.swing.JToggleButton jtbAnoPreA191;
    private javax.swing.JToggleButton jtbAnoPreA192;
    private javax.swing.JToggleButton jtbAnoPreA193;
    private javax.swing.JToggleButton jtbAnoPreA194;
    private javax.swing.JToggleButton jtbAnoPreA195;
    private javax.swing.JToggleButton jtbAnoPreA196;
    private javax.swing.JToggleButton jtbAnoPreA197;
    private javax.swing.JToggleButton jtbAnoPreA198;
    private javax.swing.JToggleButton jtbAnoPreA199;
    private javax.swing.JToggleButton jtbAnoPreA200;
    private javax.swing.JToggleButton jtbAnoPreA201;
    private javax.swing.JToggleButton jtbAnoPreA202;
    private javax.swing.JToggleButton jtbAnoPreA203;
    private javax.swing.JToggleButton jtbAnoPreA204;
    private javax.swing.JToggleButton jtbAnoPreA205;
    private javax.swing.JToggleButton jtbAnoPreA206;
    private javax.swing.JToggleButton jtbAnoPreA207;
    private javax.swing.JToggleButton jtbAnoPreA208;
    private javax.swing.JToggleButton jtbAnoPreA209;
    private javax.swing.JToggleButton jtbAnoPreA210;
    private javax.swing.JToggleButton jtbAnoPreA211;
    private javax.swing.JToggleButton jtbAnoPreA212;
    private javax.swing.JToggleButton jtbAnoPreA213;
    private javax.swing.JToggleButton jtbAnoPreA214;
    private javax.swing.JToggleButton jtbAnoPreA215;
    private javax.swing.JToggleButton jtbAnoPreA216;
    private javax.swing.JToggleButton jtbAnoPreA217;
    private javax.swing.JToggleButton jtbAnoPreA218;
    private javax.swing.JToggleButton jtbAnoPreA219;
    private javax.swing.JToggleButton jtbAnoPreA220;
    private javax.swing.JToggleButton jtbAnoPreA221;
    private javax.swing.JToggleButton jtbAnoPreA222;
    private javax.swing.JToggleButton jtbAnoPreA223;
    private javax.swing.JToggleButton jtbAnoPreA224;
    private javax.swing.JToggleButton jtbAnoPreA225;
    private javax.swing.JToggleButton jtbAnoPreA226;
    private javax.swing.JToggleButton jtbAnoPreA227;
    private javax.swing.JToggleButton jtbAnoPreA228;
    private javax.swing.JToggleButton jtbAnoPreA229;
    private javax.swing.JToggleButton jtbAnoPreA230;
    private javax.swing.JToggleButton jtbAnoPreA231;
    private javax.swing.JToggleButton jtbAnoPreA232;
    private javax.swing.JToggleButton jtbAnoPreA233;
    private javax.swing.JToggleButton jtbAnoPreA234;
    private javax.swing.JToggleButton jtbAnoPreA235;
    public static javax.swing.JTabbedPane jtpAbas1;
    public static javax.swing.JLabel lblFotEltMen8;
    private javax.swing.JTable tblArqPre;
    private javax.swing.JButton txtCanFot;
    private javax.swing.JTextField txtCodEqu3;
    private javax.swing.JTextField txtCodEqu5;
    private javax.swing.JTextField txtCodEqu6;
    private javax.swing.JTextField txtDatFin3;
    private javax.swing.JTextField txtDatFin5;
    private javax.swing.JTextField txtDatFin6;
    private javax.swing.JTextField txtDatIni3;
    private javax.swing.JTextField txtDatIni5;
    private javax.swing.JTextField txtDatIni6;
    private javax.swing.JTextField txtDatPre3;
    private javax.swing.JTextField txtDatPre5;
    private javax.swing.JTextField txtDatPre6;
    private javax.swing.JTextField txtFotPre8;
    private javax.swing.JTextField txtIdPre3;
    private javax.swing.JTextField txtIdPre5;
    private javax.swing.JTextField txtIdPre6;
    private javax.swing.JTextField txtNomTec3;
    private javax.swing.JTextField txtNomTec5;
    private javax.swing.JTextField txtNomTec6;
    private javax.swing.JTextField txtPesArq;
    private javax.swing.JTextField txtPreA119;
    private javax.swing.JTextField txtPreA120;
    private javax.swing.JTextField txtPreA121;
    private javax.swing.JTextField txtPreA122;
    private javax.swing.JTextField txtPreA123;
    private javax.swing.JTextField txtPreA124;
    private javax.swing.JTextField txtPreA125;
    private javax.swing.JTextField txtPreA126;
    private javax.swing.JTextField txtPreA127;
    private javax.swing.JTextField txtPreA128;
    private javax.swing.JTextField txtPreA129;
    private javax.swing.JTextField txtPreA130;
    private javax.swing.JTextField txtPreA131;
    private javax.swing.JTextField txtPreA132;
    private javax.swing.JTextField txtPreA133;
    private javax.swing.JTextField txtPreA134;
    private javax.swing.JTextField txtPreA135;
    private javax.swing.JTextField txtPreA136;
    private javax.swing.JTextField txtPreA137;
    private javax.swing.JTextField txtPreA138;
    private javax.swing.JTextField txtPreA139;
    private javax.swing.JTextField txtPreA140;
    private javax.swing.JTextField txtPreA141;
    private javax.swing.JTextField txtPreA142;
    private javax.swing.JTextField txtPreA143;
    private javax.swing.JTextField txtPreA144;
    private javax.swing.JTextField txtPreA145;
    private javax.swing.JTextField txtPreA173;
    private javax.swing.JTextField txtPreA174;
    private javax.swing.JTextField txtPreA175;
    private javax.swing.JTextField txtPreA176;
    private javax.swing.JTextField txtPreA177;
    private javax.swing.JTextField txtPreA178;
    private javax.swing.JTextField txtPreA179;
    private javax.swing.JTextField txtPreA180;
    private javax.swing.JTextField txtPreA181;
    private javax.swing.JTextField txtPreA182;
    private javax.swing.JTextField txtPreA183;
    private javax.swing.JTextField txtPreA184;
    private javax.swing.JTextField txtPreA185;
    private javax.swing.JTextField txtPreA186;
    private javax.swing.JTextField txtPreA187;
    private javax.swing.JTextField txtPreA188;
    private javax.swing.JTextField txtPreA189;
    private javax.swing.JTextField txtPreA190;
    private javax.swing.JTextField txtPreA191;
    private javax.swing.JTextField txtPreA192;
    private javax.swing.JTextField txtPreA193;
    private javax.swing.JTextField txtPreA194;
    private javax.swing.JTextField txtPreA195;
    private javax.swing.JTextField txtPreA196;
    private javax.swing.JTextField txtPreA197;
    private javax.swing.JTextField txtPreA198;
    private javax.swing.JTextField txtPreA199;
    private javax.swing.JTextField txtPreA200;
    private javax.swing.JTextField txtPreA201;
    private javax.swing.JTextField txtPreA202;
    private javax.swing.JTextField txtPreA203;
    private javax.swing.JTextField txtPreA204;
    private javax.swing.JTextField txtPreA205;
    private javax.swing.JTextField txtPreA206;
    private javax.swing.JTextField txtPreA207;
    private javax.swing.JTextField txtPreA208;
    private javax.swing.JTextField txtPreA209;
    private javax.swing.JTextField txtPreA210;
    private javax.swing.JTextField txtPreA211;
    private javax.swing.JTextField txtPreA212;
    private javax.swing.JTextField txtPreA213;
    private javax.swing.JTextField txtPreA214;
    private javax.swing.JTextField txtPreA215;
    private javax.swing.JTextField txtPreA216;
    private javax.swing.JTextField txtPreA217;
    private javax.swing.JTextField txtPreA218;
    private javax.swing.JTextField txtPreA219;
    private javax.swing.JTextField txtPreA220;
    private javax.swing.JTextField txtPreA221;
    private javax.swing.JTextField txtPreA222;
    private javax.swing.JTextField txtPreA223;
    private javax.swing.JTextField txtPreA224;
    private javax.swing.JTextField txtPreA225;
    private javax.swing.JTextField txtPreA226;
    private javax.swing.JTextField txtPreA227;
    private javax.swing.JTextField txtPreA228;
    private javax.swing.JTextField txtPreA229;
    private javax.swing.JTextField txtPreA230;
    private javax.swing.JTextField txtPreA231;
    private javax.swing.JTextField txtPreA232;
    private javax.swing.JTextField txtPreA233;
    private javax.swing.JTextField txtPreA234;
    private javax.swing.JTextField txtPreA235;
    private javax.swing.JTextField txtQanArq;
    private javax.swing.JTextField txtStaPre3;
    private javax.swing.JTextField txtStaPre5;
    private javax.swing.JTextField txtStaPre6;
    private javax.swing.JTextField txtTemPre3;
    private javax.swing.JTextField txtTemPre5;
    private javax.swing.JTextField txtTemPre6;
    // End of variables declaration//GEN-END:variables
}
