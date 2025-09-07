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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Classe TelaPrevEnc
 *
 * Representa uma janela interna (JInternalFrame) responsável pelo encerramento
 * de atividades preventivas. Contém atributos relacionados à conexão com banco
 * de dados, controle de arquivos, identificação de preventiva e parâmetros de
 * acesso FTP.
 *
 * Atributos: - conexao: conexão com o banco de dados. - pst: comando SQL
 * preparado. - rs: conjunto de resultados de uma consulta SQL.
 *
 * - tipo_pre, arq, id_prev, nome_pre, prev: variáveis de controle da
 * preventiva.
 *
 * - acesso: endereço IP do servidor FTP (estático). - log, sen: credenciais de
 * acesso FTP. - dire: diretório FTP de destino. - file1, file2, file3: nomes de
 * diretórios padrão para diferentes tipos de preventiva elétrica.
 *
 * - des, nom_arq, end: variáveis auxiliares para descrição, nome de arquivo e
 * caminho.
 *
 * - id1, id2, id3: identificadores estáticos utilizados em diferentes contextos
 * da aplicação. - idPrev: variável auxiliar para controle de identificador da
 * preventiva.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPrevEnc extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String tipo_pre = null;
    String arq = null;
    String id_prev = null;
    String nome_pre = null;
    String prev = null;

    public static String acesso = ("127.0.0.1");
    int porta = 21;
    String log = "servidor";
    String sen = "12345";
    String dire = null;
    String file1 = "elet_mensal";
    String file2 = "elet_trimestral";
    String file3 = "elet_semestral";

    String des = null;
    String nom_arq = null;
    String end = null;

    public static String id1 = null;
    public static String id2 = null;
    public static String id3 = null;

    String idPrev;

    /**
     * Construtor da classe TelaPrevEnc
     *
     * Responsável por inicializar a interface gráfica da janela de encerramento
     * de preventiva. Ao ser instanciado, o construtor realiza as seguintes
     * ações:
     *
     * - Chama o método `initComponents()` para configurar os componentes
     * visuais da interface. - Estabelece a conexão com o banco de dados através
     * de `ModuloConexao.conector()`. - Desabilita as três primeiras abas do
     * componente `jtpAbas`, impedindo a navegação inicial por elas.
     *
     * Essa configuração inicial garante que a interface esteja pronta para uso
     * e que apenas as abas apropriadas sejam acessíveis conforme o fluxo da
     * aplicação.
     */
    public TelaPrevEnc() {
        initComponents();

        conexao = ModuloConexao.conector();
        jtpAbas.setEnabledAt(0, false);
        jtpAbas.setEnabledAt(1, false);
        jtpAbas.setEnabledAt(2, false);
        tblArqPre.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane9.getVerticalScrollBar().setUnitIncrement(25);
        jScrollPane6.getVerticalScrollBar().setUnitIncrement(25);
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     * Método prev_encerrada
     *
     * Responsável por recuperar os dados de uma preventiva elétrica semestral
     * encerrada a partir do banco de dados, utilizando o identificador `id3`.
     *
     * Etapas executadas: - Prepara e executa uma consulta SQL que busca todos
     * os campos da tabela `form_eletrica_semestral` com base no
     * `id_form_ele_semestral`. - Se houver resultado, preenche os campos da
     * interface com os dados retornados: - Informações gerais: ID, datas,
     * tempo, status, técnico, setor, equipamento, código. - Situação e
     * observações dos itens A1 a A48. - Tipo de preventiva (`tipo_pre`).
     *
     * Observações: - A consulta SQL é extensa e retorna 107 colunas. - Os
     * campos da interface são atualizados diretamente com os valores obtidos. -
     * Em caso de erro na consulta, uma mensagem é exibida via `JOptionPane`.
     */
    public void prev_encerrada() {

        String sql = "select id_form_ele_semestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27,   a28, obser28,  a29, obser29,   a30, obser30,  a31, obser31,  a32, obser32, a33, obser33, a34, obser34, a35, obser35, a36, obser36, a37, obser37, a38, obser38, a39, obser39, a40, obser40, a41, obser41, a42, obser42, a43, obser43, a44, obser44, a45, obser45, a46, obser46, a47, obser47, a48, obser48, tipo_prev  from form_eletrica_semestral where id_form_ele_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id3);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre.setText(rs.getString(1));
                idPrev = (rs.getString(1));
                txtDatIni.setText(rs.getString(2));
                txtDatFin.setText(rs.getString(3));
                txtTemPre.setText(rs.getString(4));
                txtDatPre.setText(rs.getString(5));
                txtStaPre.setText(rs.getString(6));
                txtNomTec.setText(rs.getString(7));
                jtaSetPre.setText(rs.getString(8));
                jtaEquPre.setText(rs.getString(9));
                txtCodEqu.setText(rs.getString(10));

                txtPreA1.setText(rs.getString(11));
                jtaAnoA1.setText(rs.getString(12));

                txtPreA2.setText(rs.getString(13));
                jtaAnoA2.setText(rs.getString(14));

                txtPreA3.setText(rs.getString(15));
                jtaAnoA3.setText(rs.getString(16));

                txtPreA4.setText(rs.getString(17));
                jtaAnoA4.setText(rs.getString(18));

                txtPreA5.setText(rs.getString(19));
                jtaAnoA5.setText(rs.getString(20));

                txtPreA6.setText(rs.getString(21));
                jtaAnoA6.setText(rs.getString(22));

                txtPreA7.setText(rs.getString(23));
                jtaAnoA7.setText(rs.getString(24));

                txtPreA8.setText(rs.getString(25));
                jtaAnoA8.setText(rs.getString(26));

                txtPreA9.setText(rs.getString(27));
                jtaAnoA9.setText(rs.getString(28));

                txtPreA10.setText(rs.getString(29));
                jtaAnoA10.setText(rs.getString(30));

                txtPreA11.setText(rs.getString(31));
                jtaAnoA11.setText(rs.getString(32));

                txtPreA12.setText(rs.getString(33));
                jtaAnoA12.setText(rs.getString(34));

                txtPreA13.setText(rs.getString(35));
                jtaAnoA13.setText(rs.getString(36));

                txtPreA14.setText(rs.getString(37));
                jtaAnoA14.setText(rs.getString(38));

                txtPreA15.setText(rs.getString(39));
                jtaAnoA15.setText(rs.getString(40));

                txtPreA16.setText(rs.getString(41));
                jtaAnoA16.setText(rs.getString(42));

                txtPreA17.setText(rs.getString(43));
                jtaAnoA17.setText(rs.getString(44));

                txtPreA18.setText(rs.getString(45));
                jtaAnoA18.setText(rs.getString(46));

                txtPreA19.setText(rs.getString(47));
                jtaAnoA19.setText(rs.getString(48));

                txtPreA20.setText(rs.getString(49));
                jtaAnoA20.setText(rs.getString(50));

                txtPreA21.setText(rs.getString(51));
                jtaAnoA21.setText(rs.getString(52));

                txtPreA22.setText(rs.getString(53));
                jtaAnoA22.setText(rs.getString(54));

                txtPreA23.setText(rs.getString(55));
                jtaAnoA23.setText(rs.getString(56));

                txtPreA24.setText(rs.getString(57));
                jtaAnoA24.setText(rs.getString(58));

                txtPreA25.setText(rs.getString(59));
                jtaAnoA25.setText(rs.getString(60));

                txtPreA26.setText(rs.getString(61));
                jtaAnoA26.setText(rs.getString(62));

                txtPreA27.setText(rs.getString(63));
                jtaAnoA27.setText(rs.getString(64));

                txtPreA28.setText(rs.getString(65));
                jtaAnoA28.setText(rs.getString(66));

                txtPreA29.setText(rs.getString(67));
                jtaAnoA29.setText(rs.getString(68));

                txtPreA30.setText(rs.getString(69));
                jtaAnoA30.setText(rs.getString(70));

                txtPreA31.setText(rs.getString(71));
                jtaAnoA31.setText(rs.getString(72));

                txtPreA32.setText(rs.getString(73));
                jtaAnoA32.setText(rs.getString(74));

                txtPreA33.setText(rs.getString(75));
                jtaAnoA33.setText(rs.getString(76));

                txtPreA34.setText(rs.getString(77));
                jtaAnoA34.setText(rs.getString(78));

                txtPreA35.setText(rs.getString(79));
                jtaAnoA35.setText(rs.getString(80));

                txtPreA36.setText(rs.getString(81));
                jtaAnoA36.setText(rs.getString(82));

                txtPreA37.setText(rs.getString(83));
                jtaAnoA37.setText(rs.getString(84));

                txtPreA38.setText(rs.getString(85));
                jtaAnoA38.setText(rs.getString(86));

                txtPreA39.setText(rs.getString(87));
                jtaAnoA39.setText(rs.getString(88));

                txtPreA40.setText(rs.getString(89));
                jtaAnoA40.setText(rs.getString(90));

                txtPreA41.setText(rs.getString(91));
                jtaAnoA41.setText(rs.getString(92));

                txtPreA42.setText(rs.getString(93));
                jtaAnoA42.setText(rs.getString(94));

                txtPreA43.setText(rs.getString(95));
                jtaAnoA43.setText(rs.getString(96));

                txtPreA44.setText(rs.getString(97));
                jtaAnoA44.setText(rs.getString(98));

                txtPreA45.setText(rs.getString(99));
                jtaAnoA45.setText(rs.getString(100));

                txtPreA46.setText(rs.getString(101));
                jtaAnoA46.setText(rs.getString(102));

                txtPreA47.setText(rs.getString(103));
                jtaAnoA47.setText(rs.getString(104));

                txtPreA48.setText(rs.getString(105));
                jtaAnoA48.setText(rs.getString(106));

                tipo_pre = (rs.getString(107));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre
     *
     * Responsável por consultar e exibir os arquivos relacionados à preventiva
     * elétrica semestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o número da
     * preventiva e o nome do arquivo na tabela `arquivos_eletrica_semestral`,
     * filtrando pelo `id_form_ele_semestral` e pelo nome do arquivo que começa
     * com o valor digitado. - Prepara a instrução SQL com os parâmetros: -
     * `id3`: identificador da preventiva. - `arq + " - " + txtPesArq.getText()
     * + "%"`: padrão de busca para o nome do arquivo. - Executa a consulta e
     * atualiza a tabela `tblArqPre` com os resultados usando
     * `DbUtils.resultSetToTableModel(rs)`. - Chama o método
     * `os_encerrada_quant()` para atualizar a quantidade de OS encerradas
     * (função auxiliar).
     *
     * Em caso de erro na execução da consulta, exibe uma mensagem de erro via
     * `JOptionPane`.
     */
    private void arq_pre() {

        String sql = "select id_form_ele_semestral as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_eletrica_semestral where id_form_ele_semestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id3);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método setar_arq
     *
     * Responsável por capturar os dados da linha selecionada na tabela
     * `tblArqPre` e iniciar os processos de visualização e download do arquivo.
     *
     * Etapas executadas: - Obtém o índice da linha selecionada na tabela. -
     * Define o texto do campo `txtFotPre8` com o nome do arquivo da linha
     * selecionada. - Atualiza as variáveis `nome_pre` e `id_prev` com os
     * valores correspondentes da tabela. - Chama o método `arq_fotos()` para
     * exibir ou preparar a imagem associada. - Chama o método
     * `carregarArquivo()` para realizar o download do arquivo selecionado.
     *
     * Essa rotina é essencial para manipular arquivos vinculados à preventiva
     * encerrada.
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
     * Método os_encerrada_quant
     *
     * Responsável por calcular e exibir a quantidade de arquivos encerrados
     * relacionados à preventiva elétrica semestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que conta os registros na
     * tabela `arquivos_eletrica_semestral` filtrando pelo
     * `id_form_ele_semestral` e pelo padrão de nome do arquivo (`arq + "%"`) -
     * Prepara e executa a instrução SQL com os parâmetros `id3` e `arq + "%"`.
     * - Se houver resultado, atualiza o campo `txtQanArq` com o valor
     * retornado. - Em caso de erro na execução da consulta, exibe uma mensagem
     * de erro via `JOptionPane`.
     *
     * Esse método é útil para informar ao usuário quantos arquivos estão
     * associados à preventiva encerrada, facilitando o controle e a auditoria.
     */
    private void os_encerrada_quant() {

        String sql = "select count(*) from arquivos_eletrica_semestral where id_form_ele_semestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id3);
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
     * Responsável por recuperar o caminho (endereço) de um arquivo armazenado
     * no banco de dados, relacionado à preventiva elétrica semestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o campo
     * `endere_arq` na tabela `arquivos_eletrica_semestral`, filtrando pelo
     * identificador da preventiva (`id_form_ele_semestral`) e pelo nome do
     * arquivo. - Prepara e executa a instrução SQL com os parâmetros `id3` e o
     * texto do campo `txtFotPre8`. - Se houver resultado, atribui o valor do
     * campo `endere_arq` à variável `end`. - Em caso de erro na execução da
     * consulta, exibe uma mensagem de erro via `JOptionPane`.
     *
     * Esse método é essencial para obter o caminho do arquivo antes de realizar
     * o download físico.
     */
    private void dowload_arq() {

        String sql = "select endere_arq from arquivos_eletrica_semestral where id_form_ele_semestral = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id3);
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
     * Método prev_encerrada1
     *
     * Responsável por recuperar os dados de uma preventiva elétrica trimestral
     * encerrada a partir do banco de dados, utilizando o identificador `id2`.
     *
     * Etapas executadas: - Prepara e executa uma consulta SQL que busca todos
     * os campos da tabela `form_eletrica_trimestral` com base no
     * `id_form_ele_trimestral`. - Se houver resultado, preenche os campos da
     * interface com os dados retornados: - Informações gerais: ID, datas,
     * tempo, status, técnico, setor, equipamento, código. - Situação e
     * observações dos itens A49 a A86. - Tipo de preventiva (`tipo_pre`).
     *
     * Observações: - A consulta SQL retorna 87 colunas, cobrindo todos os itens
     * e observações da preventiva trimestral. - Os campos da interface são
     * atualizados diretamente com os valores obtidos. - Em caso de erro na
     * consulta, uma mensagem é exibida via `JOptionPane`.
     */
    public void prev_encerrada1() {

        String sql = "select id_form_ele_trimestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27,   a28, obser28,  a29, obser29,   a30, obser30,  a31, obser31,  a32, obser32, a33, obser33, a34, obser34, a35, obser35, a36, obser36, a37, obser37, a38, obser38, tipo_prev from form_eletrica_trimestral where id_form_ele_trimestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id2);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre1.setText(rs.getString(1));
                idPrev = (rs.getString(1));
                txtDatIni1.setText(rs.getString(2));
                txtDatFin1.setText(rs.getString(3));
                txtTemPre1.setText(rs.getString(4));
                txtDatPre1.setText(rs.getString(5));
                txtStaPre1.setText(rs.getString(6));
                txtNomTec1.setText(rs.getString(7));
                jtaSetPre1.setText(rs.getString(8));
                jtaEquPre1.setText(rs.getString(9));
                txtCodEqu1.setText(rs.getString(10));

                txtPreA49.setText(rs.getString(11));
                jtaAnoA49.setText(rs.getString(12));

                txtPreA50.setText(rs.getString(13));
                jtaAnoA50.setText(rs.getString(14));

                txtPreA51.setText(rs.getString(15));
                jtaAnoA51.setText(rs.getString(16));

                txtPreA52.setText(rs.getString(17));
                jtaAnoA52.setText(rs.getString(18));

                txtPreA53.setText(rs.getString(19));
                jtaAnoA53.setText(rs.getString(20));

                txtPreA54.setText(rs.getString(21));
                jtaAnoA54.setText(rs.getString(22));

                txtPreA55.setText(rs.getString(23));
                jtaAnoA55.setText(rs.getString(24));

                txtPreA56.setText(rs.getString(25));
                jtaAnoA56.setText(rs.getString(26));

                txtPreA57.setText(rs.getString(27));
                jtaAnoA57.setText(rs.getString(28));

                txtPreA58.setText(rs.getString(29));
                jtaAnoA58.setText(rs.getString(30));

                txtPreA59.setText(rs.getString(31));
                jtaAnoA59.setText(rs.getString(32));

                txtPreA60.setText(rs.getString(33));
                jtaAnoA60.setText(rs.getString(34));

                txtPreA61.setText(rs.getString(35));
                jtaAnoA61.setText(rs.getString(36));

                txtPreA62.setText(rs.getString(37));
                jtaAnoA62.setText(rs.getString(38));

                txtPreA63.setText(rs.getString(39));
                jtaAnoA63.setText(rs.getString(40));

                txtPreA64.setText(rs.getString(41));
                jtaAnoA64.setText(rs.getString(42));

                txtPreA65.setText(rs.getString(43));
                jtaAnoA65.setText(rs.getString(44));

                txtPreA66.setText(rs.getString(45));
                jtaAnoA66.setText(rs.getString(46));

                txtPreA67.setText(rs.getString(47));
                jtaAnoA67.setText(rs.getString(48));

                txtPreA68.setText(rs.getString(49));
                jtaAnoA68.setText(rs.getString(50));

                txtPreA69.setText(rs.getString(51));
                jtaAnoA69.setText(rs.getString(52));

                txtPreA70.setText(rs.getString(53));
                jtaAnoA70.setText(rs.getString(54));

                txtPreA71.setText(rs.getString(55));
                jtaAnoA71.setText(rs.getString(56));

                txtPreA72.setText(rs.getString(57));
                jtaAnoA72.setText(rs.getString(58));

                txtPreA73.setText(rs.getString(59));
                jtaAnoA73.setText(rs.getString(60));

                txtPreA74.setText(rs.getString(61));
                jtaAnoA74.setText(rs.getString(62));

                txtPreA75.setText(rs.getString(63));
                jtaAnoA75.setText(rs.getString(64));

                txtPreA76.setText(rs.getString(65));
                jtaAnoA76.setText(rs.getString(66));

                txtPreA77.setText(rs.getString(67));
                jtaAnoA77.setText(rs.getString(68));

                txtPreA78.setText(rs.getString(69));
                jtaAnoA78.setText(rs.getString(70));

                txtPreA79.setText(rs.getString(71));
                jtaAnoA79.setText(rs.getString(72));

                txtPreA80.setText(rs.getString(73));
                jtaAnoA80.setText(rs.getString(74));

                txtPreA81.setText(rs.getString(75));
                jtaAnoA82.setText(rs.getString(76));

                txtPreA82.setText(rs.getString(77));
                jtaAnoA81.setText(rs.getString(78));

                txtPreA83.setText(rs.getString(79));
                jtaAnoA83.setText(rs.getString(80));

                txtPreA84.setText(rs.getString(81));
                jtaAnoA84.setText(rs.getString(82));

                txtPreA85.setText(rs.getString(83));
                jtaAnoA85.setText(rs.getString(84));

                txtPreA86.setText(rs.getString(85));
                jtaAnoA86.setText(rs.getString(86));

                tipo_pre = (rs.getString(87));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre1
     *
     * Responsável por consultar e exibir os arquivos relacionados à preventiva
     * elétrica trimestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o número da
     * preventiva e o nome do arquivo na tabela `arquivos_eletrica_trimestral`,
     * filtrando pelo `id_form_ele_trimestral` e pelo nome do arquivo que começa
     * com o valor digitado. - Prepara a instrução SQL com os parâmetros: -
     * `id2`: identificador da preventiva. - `arq + " - " + txtPesArq.getText()
     * + "%"`: padrão de busca para o nome do arquivo. - Executa a consulta e
     * atualiza a tabela `tblArqPre` com os resultados usando
     * `DbUtils.resultSetToTableModel(rs)`. - Chama o método
     * `os_encerrada_quant1()` para atualizar a quantidade de OS encerradas
     * (função auxiliar).
     *
     * Em caso de erro na execução da consulta, exibe uma mensagem de erro via
     * `JOptionPane`.
     */
    private void arq_pre1() {

        String sql = "select id_form_ele_trimestral as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_eletrica_trimestral where id_form_ele_trimestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id2);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant1();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método os_encerrada_quant1
     *
     * Responsável por calcular e exibir a quantidade de arquivos encerrados
     * relacionados à preventiva elétrica trimestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que conta os registros na
     * tabela `arquivos_eletrica_trimestral` filtrando pelo
     * `id_form_ele_trimestral` e pelo padrão de nome do arquivo (`arq + "%"`) -
     * Prepara e executa a instrução SQL com os parâmetros `id2` e `arq + "%"`.
     * - Se houver resultado, atualiza o campo `txtQanArq` com o valor
     * retornado. - Em caso de erro na execução da consulta, exibe uma mensagem
     * de erro via `JOptionPane`.
     *
     * Esse método é útil para informar ao usuário quantos arquivos estão
     * associados à preventiva trimestral encerrada, facilitando o controle e a
     * auditoria.
     */
    private void os_encerrada_quant1() {

        String sql = "select count(*) from arquivos_eletrica_trimestral where id_form_ele_trimestral = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id2);
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
     * Responsável por recuperar o caminho (endereço) de um arquivo armazenado
     * no banco de dados, relacionado à preventiva elétrica trimestral.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o campo
     * `endere_arq` na tabela `arquivos_eletrica_trimestral`, filtrando pelo
     * identificador da preventiva (`id_form_ele_trimestral`) e pelo nome do
     * arquivo. - Prepara e executa a instrução SQL com os parâmetros `id2` e o
     * texto do campo `txtFotPre8`. - Se houver resultado, atribui o valor do
     * campo `endere_arq` à variável `end`. - Em caso de erro na execução da
     * consulta, exibe uma mensagem de erro via `JOptionPane`.
     *
     * Esse método é essencial para obter o caminho do arquivo antes de realizar
     * o download físico.
     */
    private void dowload_arq1() {

        String sql = "select endere_arq from arquivos_eletrica_trimestral where id_form_ele_trimestral = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id2);
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
     * Método prev_encerrada2
     *
     * Responsável por recuperar os dados de uma preventiva elétrica mensal
     * encerrada a partir do banco de dados, utilizando o identificador `id1`.
     *
     * Etapas executadas: - Prepara e executa uma consulta SQL que busca todos
     * os campos da tabela `form_eletrica_mensal` com base no
     * `id_form_ele_mensal`. - Se houver resultado, preenche os campos da
     * interface com os dados retornados: - Informações gerais: ID, datas,
     * tempo, status, técnico, setor, equipamento, código. - Situação e
     * observações dos itens A87 a A118. - Tipo de preventiva (`tipo_pre`).
     *
     * Observações: - A consulta SQL retorna 75 colunas, cobrindo todos os itens
     * e observações da preventiva mensal. - Os campos da interface são
     * atualizados diretamente com os valores obtidos. - Em caso de erro na
     * consulta, uma mensagem é exibida via `JOptionPane`.
     */
    public void prev_encerrada2() {

        String sql = "select id_form_ele_mensal, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27,   a28, obser28,  a29, obser29,   a30, obser30,  a31, obser31,  a32, obser32, tipo_prev from form_eletrica_mensal where id_form_ele_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id1);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtIdPre2.setText(rs.getString(1));
                idPrev = (rs.getString(1));
                txtDatIni2.setText(rs.getString(2));
                txtDatFin2.setText(rs.getString(3));
                txtTemPre2.setText(rs.getString(4));
                txtDatPre2.setText(rs.getString(5));
                txtStaPre2.setText(rs.getString(6));
                txtNomTec2.setText(rs.getString(7));
                jtaSetPre2.setText(rs.getString(8));
                jtaEquPre2.setText(rs.getString(9));
                txtCodEqu2.setText(rs.getString(10));

                txtPreA87.setText(rs.getString(11));
                jtaAnoA87.setText(rs.getString(12));

                txtPreA88.setText(rs.getString(13));
                jtaAnoA88.setText(rs.getString(14));

                txtPreA89.setText(rs.getString(15));
                jtaAnoA89.setText(rs.getString(16));

                txtPreA90.setText(rs.getString(17));
                jtaAnoA90.setText(rs.getString(18));

                txtPreA91.setText(rs.getString(19));
                jtaAnoA91.setText(rs.getString(20));

                txtPreA92.setText(rs.getString(21));
                jtaAnoA92.setText(rs.getString(22));

                txtPreA93.setText(rs.getString(23));
                jtaAnoA93.setText(rs.getString(24));

                txtPreA94.setText(rs.getString(25));
                jtaAnoA94.setText(rs.getString(26));

                txtPreA95.setText(rs.getString(27));
                jtaAnoA95.setText(rs.getString(28));

                txtPreA96.setText(rs.getString(29));
                jtaAnoA96.setText(rs.getString(30));

                txtPreA97.setText(rs.getString(31));
                jtaAnoA97.setText(rs.getString(32));

                txtPreA98.setText(rs.getString(33));
                jtaAnoA98.setText(rs.getString(34));

                txtPreA99.setText(rs.getString(35));
                jtaAnoA99.setText(rs.getString(36));

                txtPreA100.setText(rs.getString(37));
                jtaAnoA100.setText(rs.getString(38));

                txtPreA101.setText(rs.getString(39));
                jtaAnoA101.setText(rs.getString(40));

                txtPreA102.setText(rs.getString(41));
                jtaAnoA102.setText(rs.getString(42));

                txtPreA103.setText(rs.getString(43));
                jtaAnoA103.setText(rs.getString(44));

                txtPreA104.setText(rs.getString(45));
                jtaAnoA104.setText(rs.getString(46));

                txtPreA105.setText(rs.getString(47));
                jtaAnoA105.setText(rs.getString(48));

                txtPreA106.setText(rs.getString(49));
                jtaAnoA106.setText(rs.getString(50));

                txtPreA107.setText(rs.getString(51));
                jtaAnoA107.setText(rs.getString(52));

                txtPreA108.setText(rs.getString(53));
                jtaAnoA108.setText(rs.getString(54));

                txtPreA109.setText(rs.getString(55));
                jtaAnoA109.setText(rs.getString(56));

                txtPreA110.setText(rs.getString(57));
                jtaAnoA110.setText(rs.getString(58));

                txtPreA111.setText(rs.getString(59));
                jtaAnoA111.setText(rs.getString(60));

                txtPreA112.setText(rs.getString(61));
                jtaAnoA112.setText(rs.getString(62));

                txtPreA113.setText(rs.getString(63));
                jtaAnoA113.setText(rs.getString(64));

                txtPreA114.setText(rs.getString(65));
                jtaAnoA114.setText(rs.getString(66));

                txtPreA115.setText(rs.getString(67));
                jtaAnoA115.setText(rs.getString(68));

                txtPreA116.setText(rs.getString(69));
                jtaAnoA116.setText(rs.getString(70));

                txtPreA117.setText(rs.getString(71));
                jtaAnoA117.setText(rs.getString(72));

                txtPreA118.setText(rs.getString(73));
                jtaAnoA118.setText(rs.getString(74));

                tipo_pre = (rs.getString(75));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método arq_pre2
     *
     * Responsável por consultar e exibir os arquivos relacionados à preventiva
     * elétrica mensal.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o número da
     * preventiva e o nome do arquivo na tabela `arquivos_eletrica_mensal`,
     * filtrando pelo `id_form_ele_mensal` e pelo nome do arquivo que começa com
     * o valor digitado. - Prepara a instrução SQL com os parâmetros: - `id1`:
     * identificador da preventiva. - `arq + " - " + txtPesArq.getText() + "%"`:
     * padrão de busca para o nome do arquivo. - Executa a consulta e atualiza a
     * tabela `tblArqPre` com os resultados usando
     * `DbUtils.resultSetToTableModel(rs)`. - Chama o método
     * `os_encerrada_quant2()` para atualizar a quantidade de OS encerradas
     * (função auxiliar).
     *
     * Em caso de erro na execução da consulta, exibe uma mensagem de erro via
     * `JOptionPane`.
     */
    private void arq_pre2() {

        String sql = "select id_form_ele_mensal as 'N° Preventiva', nome_arq as 'Nome do Arquivo' from arquivos_eletrica_mensal where id_form_ele_mensal = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id1);
            pst.setString(2, arq + " - " + txtPesArq.getText() + "%");
            rs = pst.executeQuery();
            tblArqPre.setModel(DbUtils.resultSetToTableModel(rs));
            os_encerrada_quant2();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método os_encerrada_quant2
     *
     * Responsável por calcular e exibir a quantidade de arquivos encerrados
     * relacionados à preventiva elétrica mensal.
     *
     * Etapas executadas: - Monta uma consulta SQL que conta os registros na
     * tabela `arquivos_eletrica_mensal` filtrando pelo `id_form_ele_mensal` e
     * pelo padrão de nome do arquivo (`arq + "%"`) - Prepara e executa a
     * instrução SQL com os parâmetros `id1` e `arq + "%"`. - Se houver
     * resultado, atualiza o campo `txtQanArq` com o valor retornado. - Em caso
     * de erro na execução da consulta, exibe uma mensagem de erro via
     * `JOptionPane`.
     *
     * Esse método é útil para informar ao usuário quantos arquivos estão
     * associados à preventiva mensal encerrada, facilitando o controle e a
     * auditoria.
     */
    private void os_encerrada_quant2() {

        String sql = "select count(*) from arquivos_eletrica_mensal where id_form_ele_mensal = ? and nome_arq like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id1);
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
     * Responsável por recuperar o caminho (endereço) de um arquivo armazenado
     * no banco de dados, relacionado à preventiva elétrica mensal.
     *
     * Etapas executadas: - Monta uma consulta SQL que busca o campo
     * `endere_arq` na tabela `arquivos_eletrica_mensal`, filtrando pelo
     * identificador da preventiva (`id_form_ele_mensal`) e pelo nome do
     * arquivo. - Prepara e executa a instrução SQL com os parâmetros `id1` e o
     * texto do campo `txtFotPre8`. - Se houver resultado, atribui o valor do
     * campo `endere_arq` à variável `end`. - Em caso de erro na execução da
     * consulta, exibe uma mensagem de erro via `JOptionPane`.
     *
     * Esse método é essencial para obter o caminho do arquivo antes de realizar
     * o download físico.
     */
    private void dowload_arq2() {

        String sql = "select endere_arq from arquivos_eletrica_mensal where id_form_ele_mensal = ? and nome_arq = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id1);
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
     * Método salve_file
     *
     * Responsável por permitir ao usuário salvar um arquivo previamente
     * selecionado.
     *
     * Etapas executadas: - Obtém o nome do arquivo a partir do campo
     * `txtFotPre8`. - Inicializa um `JFileChooser` configurado para salvar
     * arquivos. - Define o nome sugerido do arquivo, removendo sua extensão. -
     * Se o usuário confirmar a operação: - Extrai a extensão original do
     * arquivo. - Atualiza o nome e caminho de destino (`des`) com a nova
     * localização e extensão. - Chama o método `download_file()` para realizar
     * o download físico. - Oculta o painel `jpaArqPre`, limpa os campos visuais
     * e desabilita o botão de salvar. - Limpa a tabela `tblArqPre` e os campos
     * de pesquisa e quantidade. - Exibe uma mensagem de sucesso via
     * `JOptionPane`. - Se o usuário cancelar, a variável `des` é definida como
     * `null`.
     *
     * Em caso de erro (como ausência de interface gráfica), uma mensagem é
     * exibida.
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
     * correspondente à preventiva (`dire + "/" + idPrev`). - Configura o modo
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

            ftp.changeWorkingDirectory(dire + "/" + idPrev);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileOutputStream fos = new FileOutputStream(des + ".jpg")) {
                if (ftp.retrieveFile(nome_pre, fos)) {

                    ImageIcon icon = new ImageIcon(des + ".jpg");
                    icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen8.getWidth(), lblFotEltMen8.getHeight(), Image.SCALE_SMOOTH));
                    lblFotEltMen8.setIcon(icon);
                    btnSalFotPre8.setEnabled(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Carregar o Arquivo.");

                }
            }

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
            String caminhoArquivo = dire.concat("/").concat(idPrev).concat("/").concat(nome_pre);

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
     * Responsável por realizar a pesquisa de arquivos relacionados à preventiva
     * elétrica, com base no tipo de preventiva selecionado (`tipo_pre`).
     *
     * Etapas executadas: - Verifica o valor da variável `tipo_pre`. - Chama o
     * método correspondente à pesquisa de arquivos: - "Eletrica - Mensal" →
     * `arq_pre2()` - "Eletrica - Trimestral" → `arq_pre1()` - "Eletrica -
     * Semestral" → `arq_pre()`
     *
     * Essa estrutura garante que a pesquisa seja direcionada à tabela correta
     * conforme o tipo de preventiva elétrica em questão.
     */
    private void pesq() {

        if (tipo_pre.equals("Eletrica - Mensal")) {
            arq_pre2();
        }
        if (tipo_pre.equals("Eletrica - Trimestral")) {
            arq_pre1();
        }
        if (tipo_pre.equals("Eletrica - Semestral")) {
            arq_pre();
        }

    }

    /**
     * Método arq_fotos
     *
     * Responsável por preparar o caminho de diretório e recuperar o endereço do
     * arquivo relacionado à preventiva elétrica, com base no tipo de preventiva
     * (`tipo_pre`).
     *
     * Etapas executadas: - Verifica o valor da variável `tipo_pre`. - Para cada
     * tipo: - Chama o método correspondente para obter o caminho do arquivo: -
     * "Eletrica - Mensal" → `dowload_arq2()` e define `dire = file1` -
     * "Eletrica - Trimestral" → `dowload_arq1()` e define `dire = file2` -
     * "Eletrica - Semestral" → `dowload_arq()` e define `dire = file3`
     *
     * Essa estrutura garante que o caminho FTP (`dire`) e o endereço do arquivo
     * (`end`) estejam corretamente definidos antes do download físico.
     */
    private void arq_fotos() {

        if (tipo_pre.equals("Eletrica - Mensal")) {
            dowload_arq2();
            dire = file1;
        }
        if (tipo_pre.equals("Eletrica - Trimestral")) {
            dowload_arq1();
            dire = file2;
        }
        if (tipo_pre.equals("Eletrica - Semestral")) {
            dowload_arq();
            dire = file3;
        }

    }

    /**
     * Realiza a impressão de relatórios com base no tipo de periodicidade
     * selecionada.
     *
     * Exibe uma caixa de diálogo para confirmação do usuário. Se confirmado,
     * carrega o relatório correspondente (mensal, trimestral ou semestral)
     * usando arquivos .jasper e preenche os dados com base no ID informado nos
     * campos de texto.
     *
     * Utiliza a biblioteca JasperReports para gerar e exibir os relatórios.
     * Também define o ícone e o título da janela de visualização.
     *
     * Em caso de erro na conversão de dados ou na geração do relatório, exibe
     * uma mensagem de erro ao usuário.
     */
    private void imprimir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/EleMen1.jasper");
                InputStream caminhoRelativo1 = getClass().getResourceAsStream("/imprimir/EleTri.jasper");
                InputStream caminhoRelativo2 = getClass().getResourceAsStream("/imprimir/EleSem.jasper");

                URL caminhoSub;
                Map<String, Object> filtro = new HashMap<>();
                JasperPrint print;
                JasperViewer viewer;
                Image icone;
//                
                switch (prev) {
                    case "mensal":

                        filtro.put("id", Integer.parseInt(txtIdPre2.getText()));
                        print = JasperFillManager.fillReport(caminhoRelativo, filtro, conexao);
                        viewer = new JasperViewer(print, false);
                        // Define o título da janela
                        viewer.setTitle("Imprimir");
                        icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                        viewer.setIconImage(icone);
                        viewer.setVisible(true);
                        break;
                    case "trimestral":

                        filtro.put("id", Integer.parseInt(txtIdPre1.getText()));

                        caminhoSub = getClass().getResource("/imprimir/");
                        filtro.put("SUBREPORT_DIR", caminhoSub);

                        // Passa a conexão do banco para ser usada pelo subrelatório
                        filtro.put("REPORT_CONNECTION", conexao);

                        print = JasperFillManager.fillReport(caminhoRelativo1, filtro, conexao);
                        viewer = new JasperViewer(print, false);
                        // Define o título da janela
                        viewer.setTitle("Imprimir");
                        icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png"));
                        viewer.setIconImage(icone);
                        viewer.setVisible(true);
                        break;
                    case "semestral":

                        filtro.put("id", Integer.parseInt(txtIdPre.getText()));

                        caminhoSub = getClass().getResource("/imprimir/");
                        filtro.put("SUBREPORT_DIR", caminhoSub);

                        // Passa a conexão do banco para ser usada pelo subrelatório
                        filtro.put("REPORT_CONNECTION", conexao);

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
        jtpAbas = new javax.swing.JTabbedPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        jpaAba3 = new javax.swing.JPanel();
        txtIdPre2 = new javax.swing.JTextField();
        txtDatIni2 = new javax.swing.JTextField();
        txtDatFin2 = new javax.swing.JTextField();
        txtTemPre2 = new javax.swing.JTextField();
        txtDatPre2 = new javax.swing.JTextField();
        txtNomTec2 = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        jtaEquPre2 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        jtaSetPre2 = new javax.swing.JTextArea();
        txtCodEqu2 = new javax.swing.JTextField();
        txtStaPre2 = new javax.swing.JTextField();
        jtbAnoPreA87 = new javax.swing.JToggleButton();
        btnFotPreA87 = new javax.swing.JButton();
        txtPreA87 = new javax.swing.JTextField();
        jspAnoPreA87 = new javax.swing.JScrollPane();
        jtaAnoA87 = new javax.swing.JTextArea();
        jspAnoPreA88 = new javax.swing.JScrollPane();
        jtaAnoA88 = new javax.swing.JTextArea();
        txtPreA88 = new javax.swing.JTextField();
        jtbAnoPreA88 = new javax.swing.JToggleButton();
        btnFotPreA88 = new javax.swing.JButton();
        txtPreA89 = new javax.swing.JTextField();
        jtbAnoPreA89 = new javax.swing.JToggleButton();
        btnFotPreA89 = new javax.swing.JButton();
        jspAnoPreA89 = new javax.swing.JScrollPane();
        jtaAnoA89 = new javax.swing.JTextArea();
        txtPreA90 = new javax.swing.JTextField();
        jtbAnoPreA90 = new javax.swing.JToggleButton();
        btnFotPreA90 = new javax.swing.JButton();
        jspAnoPreA90 = new javax.swing.JScrollPane();
        jtaAnoA90 = new javax.swing.JTextArea();
        txtPreA91 = new javax.swing.JTextField();
        jtbAnoPreA91 = new javax.swing.JToggleButton();
        btnFotPreA91 = new javax.swing.JButton();
        jspAnoPreA91 = new javax.swing.JScrollPane();
        jtaAnoA91 = new javax.swing.JTextArea();
        txtPreA92 = new javax.swing.JTextField();
        jtbAnoPreA92 = new javax.swing.JToggleButton();
        btnFotPreA92 = new javax.swing.JButton();
        jspAnoPreA92 = new javax.swing.JScrollPane();
        jtaAnoA92 = new javax.swing.JTextArea();
        txtPreA93 = new javax.swing.JTextField();
        jtbAnoPreA93 = new javax.swing.JToggleButton();
        btnFotPreA93 = new javax.swing.JButton();
        jspAnoPreA93 = new javax.swing.JScrollPane();
        jtaAnoA93 = new javax.swing.JTextArea();
        txtPreA94 = new javax.swing.JTextField();
        jtbAnoPreA94 = new javax.swing.JToggleButton();
        btnFotPreA94 = new javax.swing.JButton();
        jspAnoPreA94 = new javax.swing.JScrollPane();
        jtaAnoA94 = new javax.swing.JTextArea();
        txtPreA95 = new javax.swing.JTextField();
        jspAnoPreA95 = new javax.swing.JScrollPane();
        jtaAnoA95 = new javax.swing.JTextArea();
        jtbAnoPreA95 = new javax.swing.JToggleButton();
        btnFotPreA95 = new javax.swing.JButton();
        txtPreA96 = new javax.swing.JTextField();
        jtbAnoPreA96 = new javax.swing.JToggleButton();
        btnFotPreA96 = new javax.swing.JButton();
        jspAnoPreA96 = new javax.swing.JScrollPane();
        jtaAnoA96 = new javax.swing.JTextArea();
        txtPreA97 = new javax.swing.JTextField();
        jtbAnoPreA97 = new javax.swing.JToggleButton();
        btnFotPreA97 = new javax.swing.JButton();
        jspAnoPreA97 = new javax.swing.JScrollPane();
        jtaAnoA97 = new javax.swing.JTextArea();
        txtPreA98 = new javax.swing.JTextField();
        jtbAnoPreA98 = new javax.swing.JToggleButton();
        btnFotPreA98 = new javax.swing.JButton();
        jspAnoPreA98 = new javax.swing.JScrollPane();
        jtaAnoA98 = new javax.swing.JTextArea();
        txtPreA99 = new javax.swing.JTextField();
        jtbAnoPreA99 = new javax.swing.JToggleButton();
        btnFotPreA99 = new javax.swing.JButton();
        jspAnoPreA99 = new javax.swing.JScrollPane();
        jtaAnoA99 = new javax.swing.JTextArea();
        txtPreA100 = new javax.swing.JTextField();
        jtbAnoPreA100 = new javax.swing.JToggleButton();
        btnFotPreA100 = new javax.swing.JButton();
        jspAnoPreA100 = new javax.swing.JScrollPane();
        jtaAnoA100 = new javax.swing.JTextArea();
        txtPreA101 = new javax.swing.JTextField();
        jtbAnoPreA101 = new javax.swing.JToggleButton();
        btnFotPreA101 = new javax.swing.JButton();
        jspAnoPreA101 = new javax.swing.JScrollPane();
        jtaAnoA101 = new javax.swing.JTextArea();
        txtPreA102 = new javax.swing.JTextField();
        jtbAnoPreA102 = new javax.swing.JToggleButton();
        btnFotPreA102 = new javax.swing.JButton();
        jspAnoPreA102 = new javax.swing.JScrollPane();
        jtaAnoA102 = new javax.swing.JTextArea();
        jtbAnoPreA103 = new javax.swing.JToggleButton();
        btnFotPreA103 = new javax.swing.JButton();
        txtPreA103 = new javax.swing.JTextField();
        jspAnoPreA103 = new javax.swing.JScrollPane();
        jtaAnoA103 = new javax.swing.JTextArea();
        jspAnoPreA104 = new javax.swing.JScrollPane();
        jtaAnoA104 = new javax.swing.JTextArea();
        txtPreA104 = new javax.swing.JTextField();
        jtbAnoPreA104 = new javax.swing.JToggleButton();
        btnFotPreA104 = new javax.swing.JButton();
        txtPreA105 = new javax.swing.JTextField();
        jtbAnoPreA105 = new javax.swing.JToggleButton();
        btnFotPreA105 = new javax.swing.JButton();
        jspAnoPreA105 = new javax.swing.JScrollPane();
        jtaAnoA105 = new javax.swing.JTextArea();
        txtPreA106 = new javax.swing.JTextField();
        jtbAnoPreA106 = new javax.swing.JToggleButton();
        btnFotPreA106 = new javax.swing.JButton();
        jspAnoPreA106 = new javax.swing.JScrollPane();
        jtaAnoA106 = new javax.swing.JTextArea();
        txtPreA107 = new javax.swing.JTextField();
        jtbAnoPreA107 = new javax.swing.JToggleButton();
        btnFotPreA107 = new javax.swing.JButton();
        jspAnoPreA107 = new javax.swing.JScrollPane();
        jtaAnoA107 = new javax.swing.JTextArea();
        txtPreA108 = new javax.swing.JTextField();
        jtbAnoPreA108 = new javax.swing.JToggleButton();
        btnFotPreA108 = new javax.swing.JButton();
        jspAnoPreA108 = new javax.swing.JScrollPane();
        jtaAnoA108 = new javax.swing.JTextArea();
        txtPreA109 = new javax.swing.JTextField();
        jtbAnoPreA109 = new javax.swing.JToggleButton();
        btnFotPreA109 = new javax.swing.JButton();
        jspAnoPreA109 = new javax.swing.JScrollPane();
        jtaAnoA109 = new javax.swing.JTextArea();
        txtPreA110 = new javax.swing.JTextField();
        jtbAnoPreA110 = new javax.swing.JToggleButton();
        btnFotPreA110 = new javax.swing.JButton();
        jspAnoPreA110 = new javax.swing.JScrollPane();
        jtaAnoA110 = new javax.swing.JTextArea();
        txtPreA111 = new javax.swing.JTextField();
        jspAnoPreA111 = new javax.swing.JScrollPane();
        jtaAnoA111 = new javax.swing.JTextArea();
        jtbAnoPreA111 = new javax.swing.JToggleButton();
        btnFotPreA111 = new javax.swing.JButton();
        txtPreA112 = new javax.swing.JTextField();
        jtbAnoPreA112 = new javax.swing.JToggleButton();
        btnFotPreA112 = new javax.swing.JButton();
        jspAnoPreA112 = new javax.swing.JScrollPane();
        jtaAnoA112 = new javax.swing.JTextArea();
        txtPreA113 = new javax.swing.JTextField();
        jtbAnoPreA113 = new javax.swing.JToggleButton();
        btnFotPreA113 = new javax.swing.JButton();
        jspAnoPreA113 = new javax.swing.JScrollPane();
        jtaAnoA113 = new javax.swing.JTextArea();
        txtPreA114 = new javax.swing.JTextField();
        jtbAnoPreA114 = new javax.swing.JToggleButton();
        btnFotPreA114 = new javax.swing.JButton();
        jspAnoPreA114 = new javax.swing.JScrollPane();
        jtaAnoA114 = new javax.swing.JTextArea();
        txtPreA115 = new javax.swing.JTextField();
        jtbAnoPreA115 = new javax.swing.JToggleButton();
        btnFotPreA115 = new javax.swing.JButton();
        jspAnoPreA115 = new javax.swing.JScrollPane();
        jtaAnoA115 = new javax.swing.JTextArea();
        txtPreA116 = new javax.swing.JTextField();
        jtbAnoPreA116 = new javax.swing.JToggleButton();
        btnFotPreA116 = new javax.swing.JButton();
        jspAnoPreA116 = new javax.swing.JScrollPane();
        jtaAnoA116 = new javax.swing.JTextArea();
        txtPreA117 = new javax.swing.JTextField();
        jtbAnoPreA117 = new javax.swing.JToggleButton();
        btnFotPreA117 = new javax.swing.JButton();
        jspAnoPreA117 = new javax.swing.JScrollPane();
        jtaAnoA117 = new javax.swing.JTextArea();
        txtPreA118 = new javax.swing.JTextField();
        jtbAnoPreA118 = new javax.swing.JToggleButton();
        btnFotPreA118 = new javax.swing.JButton();
        jspAnoPreA118 = new javax.swing.JScrollPane();
        jtaAnoA118 = new javax.swing.JTextArea();
        btnImpPre2 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jpaAba2 = new javax.swing.JPanel();
        txtIdPre1 = new javax.swing.JTextField();
        txtDatIni1 = new javax.swing.JTextField();
        txtDatFin1 = new javax.swing.JTextField();
        txtTemPre1 = new javax.swing.JTextField();
        txtDatPre1 = new javax.swing.JTextField();
        txtNomTec1 = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jtaEquPre1 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jtaSetPre1 = new javax.swing.JTextArea();
        txtCodEqu1 = new javax.swing.JTextField();
        txtStaPre1 = new javax.swing.JTextField();
        jtbAnoPreA49 = new javax.swing.JToggleButton();
        btnFotPreA49 = new javax.swing.JButton();
        txtPreA49 = new javax.swing.JTextField();
        jspAnoPreA49 = new javax.swing.JScrollPane();
        jtaAnoA49 = new javax.swing.JTextArea();
        jspAnoPreA50 = new javax.swing.JScrollPane();
        jtaAnoA50 = new javax.swing.JTextArea();
        txtPreA50 = new javax.swing.JTextField();
        jtbAnoPreA50 = new javax.swing.JToggleButton();
        btnFotPreA50 = new javax.swing.JButton();
        txtPreA51 = new javax.swing.JTextField();
        jtbAnoPreA51 = new javax.swing.JToggleButton();
        btnFotPreA51 = new javax.swing.JButton();
        jspAnoPreA51 = new javax.swing.JScrollPane();
        jtaAnoA51 = new javax.swing.JTextArea();
        txtPreA52 = new javax.swing.JTextField();
        jtbAnoPreA52 = new javax.swing.JToggleButton();
        btnFotPreA52 = new javax.swing.JButton();
        jspAnoPreA52 = new javax.swing.JScrollPane();
        jtaAnoA52 = new javax.swing.JTextArea();
        txtPreA53 = new javax.swing.JTextField();
        jtbAnoPreA53 = new javax.swing.JToggleButton();
        btnFotPreA53 = new javax.swing.JButton();
        jspAnoPreA53 = new javax.swing.JScrollPane();
        jtaAnoA53 = new javax.swing.JTextArea();
        txtPreA54 = new javax.swing.JTextField();
        jtbAnoPreA54 = new javax.swing.JToggleButton();
        btnFotPreA54 = new javax.swing.JButton();
        jspAnoPreA54 = new javax.swing.JScrollPane();
        jtaAnoA54 = new javax.swing.JTextArea();
        txtPreA55 = new javax.swing.JTextField();
        jtbAnoPreA55 = new javax.swing.JToggleButton();
        btnFotPreA55 = new javax.swing.JButton();
        jspAnoPreA55 = new javax.swing.JScrollPane();
        jtaAnoA55 = new javax.swing.JTextArea();
        txtPreA56 = new javax.swing.JTextField();
        jtbAnoPreA56 = new javax.swing.JToggleButton();
        btnFotPreA56 = new javax.swing.JButton();
        jspAnoPreA56 = new javax.swing.JScrollPane();
        jtaAnoA56 = new javax.swing.JTextArea();
        txtPreA57 = new javax.swing.JTextField();
        jspAnoPreA57 = new javax.swing.JScrollPane();
        jtaAnoA57 = new javax.swing.JTextArea();
        jtbAnoPreA57 = new javax.swing.JToggleButton();
        btnFotPreA57 = new javax.swing.JButton();
        txtPreA58 = new javax.swing.JTextField();
        jtbAnoPreA58 = new javax.swing.JToggleButton();
        btnFotPreA58 = new javax.swing.JButton();
        jspAnoPreA58 = new javax.swing.JScrollPane();
        jtaAnoA58 = new javax.swing.JTextArea();
        txtPreA59 = new javax.swing.JTextField();
        jtbAnoPreA59 = new javax.swing.JToggleButton();
        btnFotPreA59 = new javax.swing.JButton();
        jspAnoPreA59 = new javax.swing.JScrollPane();
        jtaAnoA59 = new javax.swing.JTextArea();
        txtPreA60 = new javax.swing.JTextField();
        jtbAnoPreA60 = new javax.swing.JToggleButton();
        btnFotPreA60 = new javax.swing.JButton();
        jspAnoPreA60 = new javax.swing.JScrollPane();
        jtaAnoA60 = new javax.swing.JTextArea();
        txtPreA61 = new javax.swing.JTextField();
        jtbAnoPreA61 = new javax.swing.JToggleButton();
        btnFotPreA61 = new javax.swing.JButton();
        jspAnoPreA61 = new javax.swing.JScrollPane();
        jtaAnoA61 = new javax.swing.JTextArea();
        txtPreA62 = new javax.swing.JTextField();
        jtbAnoPreA62 = new javax.swing.JToggleButton();
        btnFotPreA62 = new javax.swing.JButton();
        jspAnoPreA62 = new javax.swing.JScrollPane();
        jtaAnoA62 = new javax.swing.JTextArea();
        txtPreA63 = new javax.swing.JTextField();
        jtbAnoPreA63 = new javax.swing.JToggleButton();
        btnFotPreA63 = new javax.swing.JButton();
        jspAnoPreA63 = new javax.swing.JScrollPane();
        jtaAnoA63 = new javax.swing.JTextArea();
        txtPreA64 = new javax.swing.JTextField();
        jtbAnoPreA64 = new javax.swing.JToggleButton();
        btnFotPreA64 = new javax.swing.JButton();
        jspAnoPreA64 = new javax.swing.JScrollPane();
        jtaAnoA64 = new javax.swing.JTextArea();
        jtbAnoPreA65 = new javax.swing.JToggleButton();
        btnFotPreA65 = new javax.swing.JButton();
        txtPreA65 = new javax.swing.JTextField();
        jspAnoPreA65 = new javax.swing.JScrollPane();
        jtaAnoA65 = new javax.swing.JTextArea();
        jspAnoPreA66 = new javax.swing.JScrollPane();
        jtaAnoA66 = new javax.swing.JTextArea();
        txtPreA66 = new javax.swing.JTextField();
        jtbAnoPreA66 = new javax.swing.JToggleButton();
        btnFotPreA66 = new javax.swing.JButton();
        txtPreA67 = new javax.swing.JTextField();
        jtbAnoPreA67 = new javax.swing.JToggleButton();
        btnFotPreA67 = new javax.swing.JButton();
        jspAnoPreA67 = new javax.swing.JScrollPane();
        jtaAnoA67 = new javax.swing.JTextArea();
        txtPreA68 = new javax.swing.JTextField();
        jtbAnoPreA68 = new javax.swing.JToggleButton();
        btnFotPreA68 = new javax.swing.JButton();
        jspAnoPreA68 = new javax.swing.JScrollPane();
        jtaAnoA68 = new javax.swing.JTextArea();
        txtPreA69 = new javax.swing.JTextField();
        jtbAnoPreA69 = new javax.swing.JToggleButton();
        btnFotPreA69 = new javax.swing.JButton();
        jspAnoPreA69 = new javax.swing.JScrollPane();
        jtaAnoA69 = new javax.swing.JTextArea();
        txtPreA70 = new javax.swing.JTextField();
        jtbAnoPreA70 = new javax.swing.JToggleButton();
        btnFotPreA70 = new javax.swing.JButton();
        jspAnoPreA70 = new javax.swing.JScrollPane();
        jtaAnoA70 = new javax.swing.JTextArea();
        txtPreA71 = new javax.swing.JTextField();
        jtbAnoPreA71 = new javax.swing.JToggleButton();
        btnFotPreA71 = new javax.swing.JButton();
        jspAnoPreA71 = new javax.swing.JScrollPane();
        jtaAnoA71 = new javax.swing.JTextArea();
        txtPreA72 = new javax.swing.JTextField();
        jtbAnoPreA72 = new javax.swing.JToggleButton();
        btnFotPreA72 = new javax.swing.JButton();
        jspAnoPreA72 = new javax.swing.JScrollPane();
        jtaAnoA72 = new javax.swing.JTextArea();
        txtPreA73 = new javax.swing.JTextField();
        jspAnoPreA73 = new javax.swing.JScrollPane();
        jtaAnoA73 = new javax.swing.JTextArea();
        jtbAnoPreA73 = new javax.swing.JToggleButton();
        btnFotPreA73 = new javax.swing.JButton();
        txtPreA74 = new javax.swing.JTextField();
        jtbAnoPreA74 = new javax.swing.JToggleButton();
        btnFotPreA74 = new javax.swing.JButton();
        jspAnoPreA74 = new javax.swing.JScrollPane();
        jtaAnoA74 = new javax.swing.JTextArea();
        txtPreA75 = new javax.swing.JTextField();
        jtbAnoPreA75 = new javax.swing.JToggleButton();
        btnFotPreA75 = new javax.swing.JButton();
        jspAnoPreA75 = new javax.swing.JScrollPane();
        jtaAnoA75 = new javax.swing.JTextArea();
        txtPreA76 = new javax.swing.JTextField();
        jtbAnoPreA76 = new javax.swing.JToggleButton();
        btnFotPreA76 = new javax.swing.JButton();
        jspAnoPreA76 = new javax.swing.JScrollPane();
        jtaAnoA76 = new javax.swing.JTextArea();
        txtPreA77 = new javax.swing.JTextField();
        jtbAnoPreA77 = new javax.swing.JToggleButton();
        btnFotPreA77 = new javax.swing.JButton();
        jspAnoPreA77 = new javax.swing.JScrollPane();
        jtaAnoA77 = new javax.swing.JTextArea();
        txtPreA78 = new javax.swing.JTextField();
        jtbAnoPreA78 = new javax.swing.JToggleButton();
        btnFotPreA78 = new javax.swing.JButton();
        jspAnoPreA78 = new javax.swing.JScrollPane();
        jtaAnoA78 = new javax.swing.JTextArea();
        txtPreA79 = new javax.swing.JTextField();
        jtbAnoPreA79 = new javax.swing.JToggleButton();
        btnFotPreA79 = new javax.swing.JButton();
        jspAnoPreA79 = new javax.swing.JScrollPane();
        jtaAnoA79 = new javax.swing.JTextArea();
        txtPreA80 = new javax.swing.JTextField();
        jtbAnoPreA80 = new javax.swing.JToggleButton();
        btnFotPreA80 = new javax.swing.JButton();
        jspAnoPreA80 = new javax.swing.JScrollPane();
        jtaAnoA80 = new javax.swing.JTextArea();
        jtbAnoPreA81 = new javax.swing.JToggleButton();
        btnFotPreA81 = new javax.swing.JButton();
        jspAnoPreA81 = new javax.swing.JScrollPane();
        jtaAnoA81 = new javax.swing.JTextArea();
        jspAnoPreA82 = new javax.swing.JScrollPane();
        jtaAnoA82 = new javax.swing.JTextArea();
        txtPreA81 = new javax.swing.JTextField();
        txtPreA82 = new javax.swing.JTextField();
        jtbAnoPreA82 = new javax.swing.JToggleButton();
        btnFotPreA82 = new javax.swing.JButton();
        txtPreA83 = new javax.swing.JTextField();
        jtbAnoPreA83 = new javax.swing.JToggleButton();
        btnFotPreA83 = new javax.swing.JButton();
        jspAnoPreA83 = new javax.swing.JScrollPane();
        jtaAnoA83 = new javax.swing.JTextArea();
        txtPreA84 = new javax.swing.JTextField();
        jtbAnoPreA84 = new javax.swing.JToggleButton();
        btnFotPreA84 = new javax.swing.JButton();
        jspAnoPreA84 = new javax.swing.JScrollPane();
        jtaAnoA84 = new javax.swing.JTextArea();
        txtPreA85 = new javax.swing.JTextField();
        jtbAnoPreA85 = new javax.swing.JToggleButton();
        btnFotPreA85 = new javax.swing.JButton();
        jspAnoPreA85 = new javax.swing.JScrollPane();
        jtaAnoA85 = new javax.swing.JTextArea();
        txtPreA86 = new javax.swing.JTextField();
        jtbAnoPreA86 = new javax.swing.JToggleButton();
        btnFotPreA86 = new javax.swing.JButton();
        jspAnoPreA86 = new javax.swing.JScrollPane();
        jtaAnoA86 = new javax.swing.JTextArea();
        btnImpPre1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jpaAba1 = new javax.swing.JPanel();
        txtIdPre = new javax.swing.JTextField();
        txtDatIni = new javax.swing.JTextField();
        txtDatFin = new javax.swing.JTextField();
        txtTemPre = new javax.swing.JTextField();
        txtDatPre = new javax.swing.JTextField();
        txtNomTec = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaEquPre = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtaSetPre = new javax.swing.JTextArea();
        txtCodEqu = new javax.swing.JTextField();
        txtStaPre = new javax.swing.JTextField();
        jtbAnoPreA1 = new javax.swing.JToggleButton();
        btnFotPreA1 = new javax.swing.JButton();
        jspAnoPreA1 = new javax.swing.JScrollPane();
        jtaAnoA1 = new javax.swing.JTextArea();
        txtPreA1 = new javax.swing.JTextField();
        jspAnoPreA2 = new javax.swing.JScrollPane();
        jtaAnoA2 = new javax.swing.JTextArea();
        txtPreA2 = new javax.swing.JTextField();
        jtbAnoPreA2 = new javax.swing.JToggleButton();
        btnFotPreA2 = new javax.swing.JButton();
        txtPreA3 = new javax.swing.JTextField();
        jtbAnoPreA3 = new javax.swing.JToggleButton();
        btnFotPreA3 = new javax.swing.JButton();
        jspAnoPreA3 = new javax.swing.JScrollPane();
        jtaAnoA3 = new javax.swing.JTextArea();
        txtPreA4 = new javax.swing.JTextField();
        jtbAnoPreA4 = new javax.swing.JToggleButton();
        btnFotPreA4 = new javax.swing.JButton();
        jspAnoPreA4 = new javax.swing.JScrollPane();
        jtaAnoA4 = new javax.swing.JTextArea();
        txtPreA5 = new javax.swing.JTextField();
        jtbAnoPreA5 = new javax.swing.JToggleButton();
        btnFotPreA5 = new javax.swing.JButton();
        jspAnoPreA5 = new javax.swing.JScrollPane();
        jtaAnoA5 = new javax.swing.JTextArea();
        txtPreA6 = new javax.swing.JTextField();
        jtbAnoPreA6 = new javax.swing.JToggleButton();
        btnFotPreA6 = new javax.swing.JButton();
        jspAnoPreA6 = new javax.swing.JScrollPane();
        jtaAnoA6 = new javax.swing.JTextArea();
        txtPreA7 = new javax.swing.JTextField();
        jtbAnoPreA7 = new javax.swing.JToggleButton();
        btnFotPreA7 = new javax.swing.JButton();
        jspAnoPreA7 = new javax.swing.JScrollPane();
        jtaAnoA7 = new javax.swing.JTextArea();
        txtPreA8 = new javax.swing.JTextField();
        jtbAnoPreA8 = new javax.swing.JToggleButton();
        btnFotPreA8 = new javax.swing.JButton();
        jspAnoPreA8 = new javax.swing.JScrollPane();
        jtaAnoA8 = new javax.swing.JTextArea();
        txtPreA9 = new javax.swing.JTextField();
        jspAnoPreA9 = new javax.swing.JScrollPane();
        jtaAnoA9 = new javax.swing.JTextArea();
        jtbAnoPreA9 = new javax.swing.JToggleButton();
        btnFotPreA9 = new javax.swing.JButton();
        txtPreA10 = new javax.swing.JTextField();
        jtbAnoPreA10 = new javax.swing.JToggleButton();
        btnFotPreA10 = new javax.swing.JButton();
        jspAnoPreA10 = new javax.swing.JScrollPane();
        jtaAnoA10 = new javax.swing.JTextArea();
        txtPreA11 = new javax.swing.JTextField();
        jtbAnoPreA11 = new javax.swing.JToggleButton();
        btnFotPreA11 = new javax.swing.JButton();
        jspAnoPreA11 = new javax.swing.JScrollPane();
        jtaAnoA11 = new javax.swing.JTextArea();
        txtPreA12 = new javax.swing.JTextField();
        jtbAnoPreA12 = new javax.swing.JToggleButton();
        btnFotPreA12 = new javax.swing.JButton();
        jspAnoPreA12 = new javax.swing.JScrollPane();
        jtaAnoA12 = new javax.swing.JTextArea();
        txtPreA13 = new javax.swing.JTextField();
        jtbAnoPreA13 = new javax.swing.JToggleButton();
        btnFotPreA13 = new javax.swing.JButton();
        jspAnoPreA13 = new javax.swing.JScrollPane();
        jtaAnoA13 = new javax.swing.JTextArea();
        txtPreA14 = new javax.swing.JTextField();
        jtbAnoPreA14 = new javax.swing.JToggleButton();
        btnFotPreA14 = new javax.swing.JButton();
        jspAnoPreA14 = new javax.swing.JScrollPane();
        jtaAnoA14 = new javax.swing.JTextArea();
        txtPreA15 = new javax.swing.JTextField();
        jtbAnoPreA15 = new javax.swing.JToggleButton();
        btnFotPreA15 = new javax.swing.JButton();
        jspAnoPreA15 = new javax.swing.JScrollPane();
        jtaAnoA15 = new javax.swing.JTextArea();
        txtPreA16 = new javax.swing.JTextField();
        jtbAnoPreA16 = new javax.swing.JToggleButton();
        btnFotPreA16 = new javax.swing.JButton();
        jspAnoPreA16 = new javax.swing.JScrollPane();
        jtaAnoA16 = new javax.swing.JTextArea();
        jtbAnoPreA17 = new javax.swing.JToggleButton();
        btnFotPreA17 = new javax.swing.JButton();
        txtPreA17 = new javax.swing.JTextField();
        jspAnoPreA17 = new javax.swing.JScrollPane();
        jtaAnoA17 = new javax.swing.JTextArea();
        jspAnoPreA18 = new javax.swing.JScrollPane();
        jtaAnoA18 = new javax.swing.JTextArea();
        txtPreA18 = new javax.swing.JTextField();
        jtbAnoPreA18 = new javax.swing.JToggleButton();
        btnFotPreA18 = new javax.swing.JButton();
        txtPreA19 = new javax.swing.JTextField();
        jtbAnoPreA19 = new javax.swing.JToggleButton();
        btnFotPreA19 = new javax.swing.JButton();
        jspAnoPreA19 = new javax.swing.JScrollPane();
        jtaAnoA19 = new javax.swing.JTextArea();
        txtPreA20 = new javax.swing.JTextField();
        jtbAnoPreA20 = new javax.swing.JToggleButton();
        btnFotPreA20 = new javax.swing.JButton();
        jspAnoPreA20 = new javax.swing.JScrollPane();
        jtaAnoA20 = new javax.swing.JTextArea();
        txtPreA21 = new javax.swing.JTextField();
        jtbAnoPreA21 = new javax.swing.JToggleButton();
        btnFotPreA21 = new javax.swing.JButton();
        jspAnoPreA21 = new javax.swing.JScrollPane();
        jtaAnoA21 = new javax.swing.JTextArea();
        txtPreA22 = new javax.swing.JTextField();
        jtbAnoPreA22 = new javax.swing.JToggleButton();
        btnFotPreA22 = new javax.swing.JButton();
        jspAnoPreA22 = new javax.swing.JScrollPane();
        jtaAnoA22 = new javax.swing.JTextArea();
        txtPreA23 = new javax.swing.JTextField();
        jtbAnoPreA23 = new javax.swing.JToggleButton();
        btnFotPreA23 = new javax.swing.JButton();
        jspAnoPreA23 = new javax.swing.JScrollPane();
        jtaAnoA23 = new javax.swing.JTextArea();
        txtPreA24 = new javax.swing.JTextField();
        jtbAnoPreA24 = new javax.swing.JToggleButton();
        btnFotPreA24 = new javax.swing.JButton();
        jspAnoPreA24 = new javax.swing.JScrollPane();
        jtaAnoA24 = new javax.swing.JTextArea();
        txtPreA25 = new javax.swing.JTextField();
        jspAnoPreA25 = new javax.swing.JScrollPane();
        jtaAnoA25 = new javax.swing.JTextArea();
        jtbAnoPreA25 = new javax.swing.JToggleButton();
        btnFotPreA25 = new javax.swing.JButton();
        txtPreA26 = new javax.swing.JTextField();
        jtbAnoPreA26 = new javax.swing.JToggleButton();
        btnFotPreA26 = new javax.swing.JButton();
        jspAnoPreA26 = new javax.swing.JScrollPane();
        jtaAnoA26 = new javax.swing.JTextArea();
        txtPreA27 = new javax.swing.JTextField();
        jtbAnoPreA27 = new javax.swing.JToggleButton();
        btnFotPreA27 = new javax.swing.JButton();
        jspAnoPreA27 = new javax.swing.JScrollPane();
        jtaAnoA27 = new javax.swing.JTextArea();
        txtPreA28 = new javax.swing.JTextField();
        jtbAnoPreA28 = new javax.swing.JToggleButton();
        btnFotPreA28 = new javax.swing.JButton();
        jspAnoPreA28 = new javax.swing.JScrollPane();
        jtaAnoA28 = new javax.swing.JTextArea();
        txtPreA29 = new javax.swing.JTextField();
        jtbAnoPreA29 = new javax.swing.JToggleButton();
        btnFotPreA29 = new javax.swing.JButton();
        jspAnoPreA29 = new javax.swing.JScrollPane();
        jtaAnoA29 = new javax.swing.JTextArea();
        txtPreA30 = new javax.swing.JTextField();
        jtbAnoPreA30 = new javax.swing.JToggleButton();
        btnFotPreA30 = new javax.swing.JButton();
        jspAnoPreA30 = new javax.swing.JScrollPane();
        jtaAnoA30 = new javax.swing.JTextArea();
        txtPreA31 = new javax.swing.JTextField();
        jtbAnoPreA31 = new javax.swing.JToggleButton();
        btnFotPreA31 = new javax.swing.JButton();
        jspAnoPreA31 = new javax.swing.JScrollPane();
        jtaAnoA31 = new javax.swing.JTextArea();
        txtPreA32 = new javax.swing.JTextField();
        jtbAnoPreA32 = new javax.swing.JToggleButton();
        btnFotPreA32 = new javax.swing.JButton();
        jspAnoPreA32 = new javax.swing.JScrollPane();
        jtaAnoA32 = new javax.swing.JTextArea();
        jtbAnoPreA33 = new javax.swing.JToggleButton();
        btnFotPreA33 = new javax.swing.JButton();
        txtPreA33 = new javax.swing.JTextField();
        jspAnoPreA33 = new javax.swing.JScrollPane();
        jtaAnoA33 = new javax.swing.JTextArea();
        jspAnoPreA34 = new javax.swing.JScrollPane();
        jtaAnoA34 = new javax.swing.JTextArea();
        txtPreA34 = new javax.swing.JTextField();
        jtbAnoPreA34 = new javax.swing.JToggleButton();
        btnFotPreA34 = new javax.swing.JButton();
        txtPreA35 = new javax.swing.JTextField();
        jtbAnoPreA35 = new javax.swing.JToggleButton();
        btnFotPreA35 = new javax.swing.JButton();
        jspAnoPreA35 = new javax.swing.JScrollPane();
        jtaAnoA35 = new javax.swing.JTextArea();
        txtPreA36 = new javax.swing.JTextField();
        jtbAnoPreA36 = new javax.swing.JToggleButton();
        btnFotPreA36 = new javax.swing.JButton();
        jspAnoPreA36 = new javax.swing.JScrollPane();
        jtaAnoA36 = new javax.swing.JTextArea();
        txtPreA37 = new javax.swing.JTextField();
        jtbAnoPreA37 = new javax.swing.JToggleButton();
        btnFotPreA37 = new javax.swing.JButton();
        jspAnoPreA37 = new javax.swing.JScrollPane();
        jtaAnoA37 = new javax.swing.JTextArea();
        txtPreA38 = new javax.swing.JTextField();
        jtbAnoPreA38 = new javax.swing.JToggleButton();
        btnFotPreA38 = new javax.swing.JButton();
        jspAnoPreA38 = new javax.swing.JScrollPane();
        jtaAnoA38 = new javax.swing.JTextArea();
        txtPreA39 = new javax.swing.JTextField();
        jtbAnoPreA39 = new javax.swing.JToggleButton();
        btnFotPreA39 = new javax.swing.JButton();
        jspAnoPreA39 = new javax.swing.JScrollPane();
        jtaAnoA39 = new javax.swing.JTextArea();
        txtPreA40 = new javax.swing.JTextField();
        jtbAnoPreA40 = new javax.swing.JToggleButton();
        btnFotPreA40 = new javax.swing.JButton();
        jspAnoPreA40 = new javax.swing.JScrollPane();
        jtaAnoA40 = new javax.swing.JTextArea();
        txtPreA41 = new javax.swing.JTextField();
        jspAnoPreA41 = new javax.swing.JScrollPane();
        jtaAnoA41 = new javax.swing.JTextArea();
        jtbAnoPreA41 = new javax.swing.JToggleButton();
        btnFotPreA41 = new javax.swing.JButton();
        txtPreA42 = new javax.swing.JTextField();
        jtbAnoPreA42 = new javax.swing.JToggleButton();
        btnFotPreA42 = new javax.swing.JButton();
        jspAnoPreA42 = new javax.swing.JScrollPane();
        jtaAnoA42 = new javax.swing.JTextArea();
        txtPreA43 = new javax.swing.JTextField();
        jtbAnoPreA43 = new javax.swing.JToggleButton();
        btnFotPreA43 = new javax.swing.JButton();
        jspAnoPreA43 = new javax.swing.JScrollPane();
        jtaAnoA43 = new javax.swing.JTextArea();
        txtPreA44 = new javax.swing.JTextField();
        jtbAnoPreA44 = new javax.swing.JToggleButton();
        btnFotPreA44 = new javax.swing.JButton();
        jspAnoPreA44 = new javax.swing.JScrollPane();
        jtaAnoA44 = new javax.swing.JTextArea();
        txtPreA45 = new javax.swing.JTextField();
        jtbAnoPreA45 = new javax.swing.JToggleButton();
        btnFotPreA45 = new javax.swing.JButton();
        jspAnoPreA45 = new javax.swing.JScrollPane();
        jtaAnoA45 = new javax.swing.JTextArea();
        txtPreA46 = new javax.swing.JTextField();
        jtbAnoPreA46 = new javax.swing.JToggleButton();
        btnFotPreA46 = new javax.swing.JButton();
        jspAnoPreA46 = new javax.swing.JScrollPane();
        jtaAnoA46 = new javax.swing.JTextArea();
        txtPreA47 = new javax.swing.JTextField();
        jtbAnoPreA47 = new javax.swing.JToggleButton();
        btnFotPreA47 = new javax.swing.JButton();
        jspAnoPreA47 = new javax.swing.JScrollPane();
        jtaAnoA47 = new javax.swing.JTextArea();
        txtPreA48 = new javax.swing.JTextField();
        jtbAnoPreA48 = new javax.swing.JToggleButton();
        btnFotPreA48 = new javax.swing.JButton();
        jspAnoPreA48 = new javax.swing.JScrollPane();
        jtaAnoA48 = new javax.swing.JTextArea();
        btnImpPre = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();

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

        jtpAbas.setName(""); // NOI18N

        jScrollPane9.setPreferredSize(new java.awt.Dimension(962, 2318));

        jpaAba3.setPreferredSize(new java.awt.Dimension(960, 2312));
        jpaAba3.setLayout(null);

        txtIdPre2.setEditable(false);
        txtIdPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtIdPre2);
        txtIdPre2.setBounds(58, 182, 100, 28);

        txtDatIni2.setEditable(false);
        txtDatIni2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtDatIni2);
        txtDatIni2.setBounds(188, 182, 170, 28);

        txtDatFin2.setEditable(false);
        txtDatFin2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtDatFin2);
        txtDatFin2.setBounds(388, 182, 170, 28);

        txtTemPre2.setEditable(false);
        txtTemPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtTemPre2);
        txtTemPre2.setBounds(588, 182, 170, 28);

        txtDatPre2.setEditable(false);
        txtDatPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtDatPre2);
        txtDatPre2.setBounds(788, 182, 140, 28);

        txtNomTec2.setEditable(false);
        txtNomTec2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtNomTec2);
        txtNomTec2.setBounds(58, 243, 180, 28);

        jScrollPane10.setEnabled(false);

        jtaEquPre2.setEditable(false);
        jtaEquPre2.setColumns(20);
        jtaEquPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre2.setLineWrap(true);
        jtaEquPre2.setRows(5);
        jScrollPane10.setViewportView(jtaEquPre2);

        jpaAba3.add(jScrollPane10);
        jScrollPane10.setBounds(477, 238, 184, 38);

        jScrollPane11.setEnabled(false);

        jtaSetPre2.setEditable(false);
        jtaSetPre2.setColumns(20);
        jtaSetPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre2.setLineWrap(true);
        jtaSetPre2.setRows(5);
        jScrollPane11.setViewportView(jtaSetPre2);

        jpaAba3.add(jScrollPane11);
        jScrollPane11.setBounds(266, 238, 184, 38);

        txtCodEqu2.setEditable(false);
        txtCodEqu2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtCodEqu2);
        txtCodEqu2.setBounds(688, 243, 100, 28);

        txtStaPre2.setEditable(false);
        txtStaPre2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtStaPre2);
        txtStaPre2.setBounds(818, 243, 110, 28);

        jtbAnoPreA87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA87ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA87);
        jtbAnoPreA87.setBounds(824, 391, 40, 40);

        btnFotPreA87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA87ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA87);
        btnFotPreA87.setBounds(880, 391, 40, 40);

        txtPreA87.setEditable(false);
        txtPreA87.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA87.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA87);
        txtPreA87.setBounds(704, 397, 109, 28);

        jtaAnoA87.setEditable(false);
        jtaAnoA87.setColumns(20);
        jtaAnoA87.setLineWrap(true);
        jtaAnoA87.setRows(5);
        jspAnoPreA87.setViewportView(jtaAnoA87);

        jpaAba3.add(jspAnoPreA87);
        jspAnoPreA87.setBounds(750, 428, 40, 10);

        jtaAnoA88.setEditable(false);
        jtaAnoA88.setColumns(20);
        jtaAnoA88.setLineWrap(true);
        jtaAnoA88.setRows(5);
        jspAnoPreA88.setViewportView(jtaAnoA88);

        jpaAba3.add(jspAnoPreA88);
        jspAnoPreA88.setBounds(750, 481, 40, 10);

        txtPreA88.setEditable(false);
        txtPreA88.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA88.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA88);
        txtPreA88.setBounds(704, 450, 109, 28);

        jtbAnoPreA88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA88ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA88);
        jtbAnoPreA88.setBounds(824, 444, 40, 40);

        btnFotPreA88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA88ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA88);
        btnFotPreA88.setBounds(880, 444, 40, 40);

        txtPreA89.setEditable(false);
        txtPreA89.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA89.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA89);
        txtPreA89.setBounds(704, 504, 109, 28);

        jtbAnoPreA89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA89ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA89);
        jtbAnoPreA89.setBounds(824, 498, 40, 40);

        btnFotPreA89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA89ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA89);
        btnFotPreA89.setBounds(880, 498, 40, 40);

        jtaAnoA89.setEditable(false);
        jtaAnoA89.setColumns(20);
        jtaAnoA89.setLineWrap(true);
        jtaAnoA89.setRows(5);
        jspAnoPreA89.setViewportView(jtaAnoA89);

        jpaAba3.add(jspAnoPreA89);
        jspAnoPreA89.setBounds(750, 535, 40, 10);

        txtPreA90.setEditable(false);
        txtPreA90.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA90.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA90);
        txtPreA90.setBounds(704, 556, 109, 28);

        jtbAnoPreA90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA90ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA90);
        jtbAnoPreA90.setBounds(824, 550, 40, 40);

        btnFotPreA90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA90ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA90);
        btnFotPreA90.setBounds(880, 550, 40, 40);

        jtaAnoA90.setEditable(false);
        jtaAnoA90.setColumns(20);
        jtaAnoA90.setLineWrap(true);
        jtaAnoA90.setRows(5);
        jspAnoPreA90.setViewportView(jtaAnoA90);

        jpaAba3.add(jspAnoPreA90);
        jspAnoPreA90.setBounds(750, 587, 40, 10);

        txtPreA91.setEditable(false);
        txtPreA91.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA91.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA91);
        txtPreA91.setBounds(704, 610, 109, 28);

        jtbAnoPreA91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA91ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA91);
        jtbAnoPreA91.setBounds(824, 603, 40, 40);

        btnFotPreA91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA91ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA91);
        btnFotPreA91.setBounds(880, 603, 40, 40);

        jtaAnoA91.setEditable(false);
        jtaAnoA91.setColumns(20);
        jtaAnoA91.setLineWrap(true);
        jtaAnoA91.setRows(5);
        jspAnoPreA91.setViewportView(jtaAnoA91);

        jpaAba3.add(jspAnoPreA91);
        jspAnoPreA91.setBounds(750, 640, 40, 10);

        txtPreA92.setEditable(false);
        txtPreA92.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA92.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA92);
        txtPreA92.setBounds(704, 660, 109, 28);

        jtbAnoPreA92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA92ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA92);
        jtbAnoPreA92.setBounds(824, 656, 40, 40);

        btnFotPreA92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA92ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA92);
        btnFotPreA92.setBounds(880, 656, 40, 40);

        jtaAnoA92.setEditable(false);
        jtaAnoA92.setColumns(20);
        jtaAnoA92.setLineWrap(true);
        jtaAnoA92.setRows(5);
        jspAnoPreA92.setViewportView(jtaAnoA92);

        jpaAba3.add(jspAnoPreA92);
        jspAnoPreA92.setBounds(750, 693, 40, 10);

        txtPreA93.setEditable(false);
        txtPreA93.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA93.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA93);
        txtPreA93.setBounds(704, 716, 109, 28);

        jtbAnoPreA93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA93ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA93);
        jtbAnoPreA93.setBounds(824, 709, 40, 40);

        btnFotPreA93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA93ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA93);
        btnFotPreA93.setBounds(880, 709, 40, 40);

        jtaAnoA93.setEditable(false);
        jtaAnoA93.setColumns(20);
        jtaAnoA93.setLineWrap(true);
        jtaAnoA93.setRows(5);
        jspAnoPreA93.setViewportView(jtaAnoA93);

        jpaAba3.add(jspAnoPreA93);
        jspAnoPreA93.setBounds(750, 746, 40, 10);

        txtPreA94.setEditable(false);
        txtPreA94.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA94.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA94);
        txtPreA94.setBounds(704, 770, 109, 28);

        jtbAnoPreA94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA94ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA94);
        jtbAnoPreA94.setBounds(824, 763, 40, 40);

        btnFotPreA94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA94ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA94);
        btnFotPreA94.setBounds(880, 763, 40, 40);

        jtaAnoA94.setEditable(false);
        jtaAnoA94.setColumns(20);
        jtaAnoA94.setLineWrap(true);
        jtaAnoA94.setRows(5);
        jspAnoPreA94.setViewportView(jtaAnoA94);

        jpaAba3.add(jspAnoPreA94);
        jspAnoPreA94.setBounds(750, 800, 40, 10);

        txtPreA95.setEditable(false);
        txtPreA95.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA95.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA95);
        txtPreA95.setBounds(704, 822, 109, 28);

        jtaAnoA95.setEditable(false);
        jtaAnoA95.setColumns(20);
        jtaAnoA95.setLineWrap(true);
        jtaAnoA95.setRows(5);
        jspAnoPreA95.setViewportView(jtaAnoA95);

        jpaAba3.add(jspAnoPreA95);
        jspAnoPreA95.setBounds(750, 853, 40, 10);

        jtbAnoPreA95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA95ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA95);
        jtbAnoPreA95.setBounds(824, 816, 40, 40);

        btnFotPreA95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA95ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA95);
        btnFotPreA95.setBounds(880, 816, 40, 40);

        txtPreA96.setEditable(false);
        txtPreA96.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA96.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA96);
        txtPreA96.setBounds(704, 875, 109, 28);

        jtbAnoPreA96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA96ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA96);
        jtbAnoPreA96.setBounds(824, 868, 40, 40);

        btnFotPreA96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA96ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA96);
        btnFotPreA96.setBounds(880, 868, 40, 40);

        jtaAnoA96.setEditable(false);
        jtaAnoA96.setColumns(20);
        jtaAnoA96.setLineWrap(true);
        jtaAnoA96.setRows(5);
        jspAnoPreA96.setViewportView(jtaAnoA96);

        jpaAba3.add(jspAnoPreA96);
        jspAnoPreA96.setBounds(750, 906, 40, 10);

        txtPreA97.setEditable(false);
        txtPreA97.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA97.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA97);
        txtPreA97.setBounds(704, 928, 109, 28);

        jtbAnoPreA97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA97ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA97);
        jtbAnoPreA97.setBounds(824, 922, 40, 40);

        btnFotPreA97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA97ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA97);
        btnFotPreA97.setBounds(880, 922, 40, 40);

        jtaAnoA97.setEditable(false);
        jtaAnoA97.setColumns(20);
        jtaAnoA97.setLineWrap(true);
        jtaAnoA97.setRows(5);
        jspAnoPreA97.setViewportView(jtaAnoA97);

        jpaAba3.add(jspAnoPreA97);
        jspAnoPreA97.setBounds(750, 959, 40, 10);

        txtPreA98.setEditable(false);
        txtPreA98.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA98.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA98);
        txtPreA98.setBounds(704, 980, 109, 28);

        jtbAnoPreA98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA98ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA98);
        jtbAnoPreA98.setBounds(824, 974, 40, 40);

        btnFotPreA98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA98ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA98);
        btnFotPreA98.setBounds(880, 974, 40, 40);

        jtaAnoA98.setEditable(false);
        jtaAnoA98.setColumns(20);
        jtaAnoA98.setLineWrap(true);
        jtaAnoA98.setRows(5);
        jspAnoPreA98.setViewportView(jtaAnoA98);

        jpaAba3.add(jspAnoPreA98);
        jspAnoPreA98.setBounds(750, 1012, 40, 10);

        txtPreA99.setEditable(false);
        txtPreA99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA99.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA99);
        txtPreA99.setBounds(704, 1034, 109, 28);

        jtbAnoPreA99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA99ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA99);
        jtbAnoPreA99.setBounds(824, 1028, 40, 40);

        btnFotPreA99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA99ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA99);
        btnFotPreA99.setBounds(880, 1028, 40, 40);

        jtaAnoA99.setEditable(false);
        jtaAnoA99.setColumns(20);
        jtaAnoA99.setLineWrap(true);
        jtaAnoA99.setRows(5);
        jspAnoPreA99.setViewportView(jtaAnoA99);

        jpaAba3.add(jspAnoPreA99);
        jspAnoPreA99.setBounds(750, 1065, 40, 10);

        txtPreA100.setEditable(false);
        txtPreA100.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA100.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA100);
        txtPreA100.setBounds(704, 1086, 109, 28);

        jtbAnoPreA100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA100ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA100);
        jtbAnoPreA100.setBounds(824, 1080, 40, 40);

        btnFotPreA100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA100ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA100);
        btnFotPreA100.setBounds(880, 1080, 40, 40);

        jtaAnoA100.setEditable(false);
        jtaAnoA100.setColumns(20);
        jtaAnoA100.setLineWrap(true);
        jtaAnoA100.setRows(5);
        jspAnoPreA100.setViewportView(jtaAnoA100);

        jpaAba3.add(jspAnoPreA100);
        jspAnoPreA100.setBounds(750, 1118, 40, 10);

        txtPreA101.setEditable(false);
        txtPreA101.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA101.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA101);
        txtPreA101.setBounds(704, 1140, 109, 28);

        jtbAnoPreA101.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA101ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA101);
        jtbAnoPreA101.setBounds(824, 1133, 40, 40);

        btnFotPreA101.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA101ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA101);
        btnFotPreA101.setBounds(880, 1133, 40, 40);

        jtaAnoA101.setEditable(false);
        jtaAnoA101.setColumns(20);
        jtaAnoA101.setLineWrap(true);
        jtaAnoA101.setRows(5);
        jspAnoPreA101.setViewportView(jtaAnoA101);

        jpaAba3.add(jspAnoPreA101);
        jspAnoPreA101.setBounds(750, 1171, 40, 10);

        txtPreA102.setEditable(false);
        txtPreA102.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA102.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA102);
        txtPreA102.setBounds(704, 1192, 109, 28);

        jtbAnoPreA102.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA102ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA102);
        jtbAnoPreA102.setBounds(824, 1186, 40, 40);

        btnFotPreA102.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA102ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA102);
        btnFotPreA102.setBounds(880, 1186, 40, 40);

        jtaAnoA102.setEditable(false);
        jtaAnoA102.setColumns(20);
        jtaAnoA102.setLineWrap(true);
        jtaAnoA102.setRows(5);
        jspAnoPreA102.setViewportView(jtaAnoA102);

        jpaAba3.add(jspAnoPreA102);
        jspAnoPreA102.setBounds(750, 1223, 40, 10);

        jtbAnoPreA103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA103ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA103);
        jtbAnoPreA103.setBounds(824, 1240, 40, 40);

        btnFotPreA103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA103ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA103);
        btnFotPreA103.setBounds(880, 1240, 40, 40);

        txtPreA103.setEditable(false);
        txtPreA103.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA103.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA103);
        txtPreA103.setBounds(704, 1246, 109, 28);

        jtaAnoA103.setEditable(false);
        jtaAnoA103.setColumns(20);
        jtaAnoA103.setLineWrap(true);
        jtaAnoA103.setRows(5);
        jspAnoPreA103.setViewportView(jtaAnoA103);

        jpaAba3.add(jspAnoPreA103);
        jspAnoPreA103.setBounds(750, 1276, 40, 10);

        jtaAnoA104.setEditable(false);
        jtaAnoA104.setColumns(20);
        jtaAnoA104.setLineWrap(true);
        jtaAnoA104.setRows(5);
        jspAnoPreA104.setViewportView(jtaAnoA104);

        jpaAba3.add(jspAnoPreA104);
        jspAnoPreA104.setBounds(750, 1329, 40, 10);

        txtPreA104.setEditable(false);
        txtPreA104.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA104.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA104);
        txtPreA104.setBounds(704, 1298, 109, 28);

        jtbAnoPreA104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA104ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA104);
        jtbAnoPreA104.setBounds(824, 1292, 40, 40);

        btnFotPreA104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA104ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA104);
        btnFotPreA104.setBounds(880, 1292, 40, 40);

        txtPreA105.setEditable(false);
        txtPreA105.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA105.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA105);
        txtPreA105.setBounds(704, 1352, 109, 28);

        jtbAnoPreA105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA105ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA105);
        jtbAnoPreA105.setBounds(824, 1345, 40, 40);

        btnFotPreA105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA105ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA105);
        btnFotPreA105.setBounds(880, 1345, 40, 40);

        jtaAnoA105.setEditable(false);
        jtaAnoA105.setColumns(20);
        jtaAnoA105.setLineWrap(true);
        jtaAnoA105.setRows(5);
        jspAnoPreA105.setViewportView(jtaAnoA105);

        jpaAba3.add(jspAnoPreA105);
        jspAnoPreA105.setBounds(750, 1383, 40, 10);

        txtPreA106.setEditable(false);
        txtPreA106.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA106.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA106);
        txtPreA106.setBounds(704, 1406, 109, 28);

        jtbAnoPreA106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA106.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA106ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA106);
        jtbAnoPreA106.setBounds(824, 1398, 40, 40);

        btnFotPreA106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA106.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA106ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA106);
        btnFotPreA106.setBounds(880, 1398, 40, 40);

        jtaAnoA106.setEditable(false);
        jtaAnoA106.setColumns(20);
        jtaAnoA106.setLineWrap(true);
        jtaAnoA106.setRows(5);
        jspAnoPreA106.setViewportView(jtaAnoA106);

        jpaAba3.add(jspAnoPreA106);
        jspAnoPreA106.setBounds(750, 1436, 40, 10);

        txtPreA107.setEditable(false);
        txtPreA107.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA107.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA107);
        txtPreA107.setBounds(704, 1458, 109, 28);

        jtbAnoPreA107.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA107.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA107ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA107);
        jtbAnoPreA107.setBounds(824, 1452, 40, 40);

        btnFotPreA107.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA107.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA107ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA107);
        btnFotPreA107.setBounds(880, 1452, 40, 40);

        jtaAnoA107.setEditable(false);
        jtaAnoA107.setColumns(20);
        jtaAnoA107.setLineWrap(true);
        jtaAnoA107.setRows(5);
        jspAnoPreA107.setViewportView(jtaAnoA107);

        jpaAba3.add(jspAnoPreA107);
        jspAnoPreA107.setBounds(750, 1489, 40, 10);

        txtPreA108.setEditable(false);
        txtPreA108.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA108.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA108);
        txtPreA108.setBounds(704, 1510, 109, 28);

        jtbAnoPreA108.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA108ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA108);
        jtbAnoPreA108.setBounds(824, 1505, 40, 40);

        btnFotPreA108.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA108ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA108);
        btnFotPreA108.setBounds(880, 1505, 40, 40);

        jtaAnoA108.setEditable(false);
        jtaAnoA108.setColumns(20);
        jtaAnoA108.setLineWrap(true);
        jtaAnoA108.setRows(5);
        jspAnoPreA108.setViewportView(jtaAnoA108);

        jpaAba3.add(jspAnoPreA108);
        jspAnoPreA108.setBounds(750, 1542, 40, 10);

        txtPreA109.setEditable(false);
        txtPreA109.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA109.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA109);
        txtPreA109.setBounds(704, 1564, 109, 28);

        jtbAnoPreA109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA109ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA109);
        jtbAnoPreA109.setBounds(824, 1557, 40, 40);

        btnFotPreA109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA109ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA109);
        btnFotPreA109.setBounds(880, 1557, 40, 40);

        jtaAnoA109.setEditable(false);
        jtaAnoA109.setColumns(20);
        jtaAnoA109.setLineWrap(true);
        jtaAnoA109.setRows(5);
        jspAnoPreA109.setViewportView(jtaAnoA109);

        jpaAba3.add(jspAnoPreA109);
        jspAnoPreA109.setBounds(750, 1595, 40, 10);

        txtPreA110.setEditable(false);
        txtPreA110.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA110.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA110);
        txtPreA110.setBounds(704, 1616, 109, 28);

        jtbAnoPreA110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA110ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA110);
        jtbAnoPreA110.setBounds(824, 1610, 40, 40);

        btnFotPreA110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA110ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA110);
        btnFotPreA110.setBounds(880, 1610, 40, 40);

        jtaAnoA110.setEditable(false);
        jtaAnoA110.setColumns(20);
        jtaAnoA110.setLineWrap(true);
        jtaAnoA110.setRows(5);
        jspAnoPreA110.setViewportView(jtaAnoA110);

        jpaAba3.add(jspAnoPreA110);
        jspAnoPreA110.setBounds(750, 1648, 40, 10);

        txtPreA111.setEditable(false);
        txtPreA111.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA111.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA111);
        txtPreA111.setBounds(704, 1670, 109, 28);

        jtaAnoA111.setEditable(false);
        jtaAnoA111.setColumns(20);
        jtaAnoA111.setLineWrap(true);
        jtaAnoA111.setRows(5);
        jspAnoPreA111.setViewportView(jtaAnoA111);

        jpaAba3.add(jspAnoPreA111);
        jspAnoPreA111.setBounds(750, 1701, 40, 10);

        jtbAnoPreA111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA111ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA111);
        jtbAnoPreA111.setBounds(824, 1663, 40, 40);

        btnFotPreA111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA111ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA111);
        btnFotPreA111.setBounds(880, 1663, 40, 40);

        txtPreA112.setEditable(false);
        txtPreA112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA112.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA112);
        txtPreA112.setBounds(704, 1723, 109, 28);

        jtbAnoPreA112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA112ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA112);
        jtbAnoPreA112.setBounds(824, 1716, 40, 40);

        btnFotPreA112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA112ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA112);
        btnFotPreA112.setBounds(880, 1716, 40, 40);

        jtaAnoA112.setEditable(false);
        jtaAnoA112.setColumns(20);
        jtaAnoA112.setLineWrap(true);
        jtaAnoA112.setRows(5);
        jspAnoPreA112.setViewportView(jtaAnoA112);

        jpaAba3.add(jspAnoPreA112);
        jspAnoPreA112.setBounds(750, 1754, 40, 10);

        txtPreA113.setEditable(false);
        txtPreA113.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA113.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA113);
        txtPreA113.setBounds(704, 1776, 109, 28);

        jtbAnoPreA113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA113ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA113);
        jtbAnoPreA113.setBounds(824, 1770, 40, 40);

        btnFotPreA113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA113ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA113);
        btnFotPreA113.setBounds(880, 1770, 40, 40);

        jtaAnoA113.setEditable(false);
        jtaAnoA113.setColumns(20);
        jtaAnoA113.setLineWrap(true);
        jtaAnoA113.setRows(5);
        jspAnoPreA113.setViewportView(jtaAnoA113);

        jpaAba3.add(jspAnoPreA113);
        jspAnoPreA113.setBounds(750, 1807, 40, 10);

        txtPreA114.setEditable(false);
        txtPreA114.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA114.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA114);
        txtPreA114.setBounds(704, 1828, 109, 28);

        jtbAnoPreA114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA114ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA114);
        jtbAnoPreA114.setBounds(824, 1823, 40, 40);

        btnFotPreA114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA114ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA114);
        btnFotPreA114.setBounds(880, 1823, 40, 40);

        jtaAnoA114.setEditable(false);
        jtaAnoA114.setColumns(20);
        jtaAnoA114.setLineWrap(true);
        jtaAnoA114.setRows(5);
        jspAnoPreA114.setViewportView(jtaAnoA114);

        jpaAba3.add(jspAnoPreA114);
        jspAnoPreA114.setBounds(750, 1860, 40, 10);

        txtPreA115.setEditable(false);
        txtPreA115.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA115.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA115);
        txtPreA115.setBounds(704, 1882, 109, 28);

        jtbAnoPreA115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA115ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA115);
        jtbAnoPreA115.setBounds(824, 1876, 40, 40);

        btnFotPreA115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA115ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA115);
        btnFotPreA115.setBounds(880, 1876, 40, 40);

        jtaAnoA115.setEditable(false);
        jtaAnoA115.setColumns(20);
        jtaAnoA115.setLineWrap(true);
        jtaAnoA115.setRows(5);
        jspAnoPreA115.setViewportView(jtaAnoA115);

        jpaAba3.add(jspAnoPreA115);
        jspAnoPreA115.setBounds(750, 1913, 40, 10);

        txtPreA116.setEditable(false);
        txtPreA116.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA116.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA116);
        txtPreA116.setBounds(704, 1934, 109, 28);

        jtbAnoPreA116.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA116ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA116);
        jtbAnoPreA116.setBounds(824, 1928, 40, 40);

        btnFotPreA116.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA116ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA116);
        btnFotPreA116.setBounds(880, 1928, 40, 40);

        jtaAnoA116.setEditable(false);
        jtaAnoA116.setColumns(20);
        jtaAnoA116.setLineWrap(true);
        jtaAnoA116.setRows(5);
        jspAnoPreA116.setViewportView(jtaAnoA116);

        jpaAba3.add(jspAnoPreA116);
        jspAnoPreA116.setBounds(750, 1966, 40, 10);

        txtPreA117.setEditable(false);
        txtPreA117.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA117.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA117);
        txtPreA117.setBounds(704, 1988, 109, 28);

        jtbAnoPreA117.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA117.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA117ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA117);
        jtbAnoPreA117.setBounds(824, 1982, 40, 40);

        btnFotPreA117.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA117.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA117ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA117);
        btnFotPreA117.setBounds(880, 1982, 40, 40);

        jtaAnoA117.setEditable(false);
        jtaAnoA117.setColumns(20);
        jtaAnoA117.setLineWrap(true);
        jtaAnoA117.setRows(5);
        jspAnoPreA117.setViewportView(jtaAnoA117);

        jpaAba3.add(jspAnoPreA117);
        jspAnoPreA117.setBounds(750, 2019, 40, 10);

        txtPreA118.setEditable(false);
        txtPreA118.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA118.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba3.add(txtPreA118);
        txtPreA118.setBounds(704, 2040, 109, 28);

        jtbAnoPreA118.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA118ActionPerformed(evt);
            }
        });
        jpaAba3.add(jtbAnoPreA118);
        jtbAnoPreA118.setBounds(824, 2034, 40, 40);

        btnFotPreA118.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA118ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnFotPreA118);
        btnFotPreA118.setBounds(880, 2034, 40, 40);

        jtaAnoA118.setEditable(false);
        jtaAnoA118.setColumns(20);
        jtaAnoA118.setLineWrap(true);
        jtaAnoA118.setRows(5);
        jspAnoPreA118.setViewportView(jtaAnoA118);

        jpaAba3.add(jspAnoPreA118);
        jspAnoPreA118.setBounds(750, 2072, 40, 10);

        btnImpPre2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre2.setToolTipText("Imprimir Documento");
        btnImpPre2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPre2ActionPerformed(evt);
            }
        });
        jpaAba3.add(btnImpPre2);
        btnImpPre2.setBounds(822, 2194, 64, 64);

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Elétrica mensal2 – 1.png"))); // NOI18N
        jpaAba3.add(jLabel20);
        jLabel20.setBounds(-12, 0, 1000, 2321);

        jScrollPane9.setViewportView(jpaAba3);

        jtpAbas.addTab("Eletrica Mensal", jScrollPane9);

        jpaAba2.setPreferredSize(new java.awt.Dimension(960, 2640));
        jpaAba2.setLayout(null);

        txtIdPre1.setEditable(false);
        txtIdPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtIdPre1);
        txtIdPre1.setBounds(58, 182, 100, 28);

        txtDatIni1.setEditable(false);
        txtDatIni1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtDatIni1);
        txtDatIni1.setBounds(188, 182, 170, 28);

        txtDatFin1.setEditable(false);
        txtDatFin1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtDatFin1);
        txtDatFin1.setBounds(388, 182, 170, 28);

        txtTemPre1.setEditable(false);
        txtTemPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtTemPre1);
        txtTemPre1.setBounds(588, 182, 170, 28);

        txtDatPre1.setEditable(false);
        txtDatPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtDatPre1);
        txtDatPre1.setBounds(788, 182, 140, 28);

        txtNomTec1.setEditable(false);
        txtNomTec1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtNomTec1);
        txtNomTec1.setBounds(58, 243, 180, 28);

        jScrollPane7.setEnabled(false);

        jtaEquPre1.setEditable(false);
        jtaEquPre1.setColumns(20);
        jtaEquPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre1.setLineWrap(true);
        jtaEquPre1.setRows(5);
        jScrollPane7.setViewportView(jtaEquPre1);

        jpaAba2.add(jScrollPane7);
        jScrollPane7.setBounds(477, 238, 184, 38);

        jScrollPane8.setEnabled(false);

        jtaSetPre1.setEditable(false);
        jtaSetPre1.setColumns(20);
        jtaSetPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre1.setLineWrap(true);
        jtaSetPre1.setRows(5);
        jScrollPane8.setViewportView(jtaSetPre1);

        jpaAba2.add(jScrollPane8);
        jScrollPane8.setBounds(266, 238, 184, 38);

        txtCodEqu1.setEditable(false);
        txtCodEqu1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtCodEqu1);
        txtCodEqu1.setBounds(688, 243, 100, 28);

        txtStaPre1.setEditable(false);
        txtStaPre1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtStaPre1);
        txtStaPre1.setBounds(818, 243, 110, 28);

        jtbAnoPreA49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA49ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA49);
        jtbAnoPreA49.setBounds(824, 391, 40, 40);

        btnFotPreA49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA49ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA49);
        btnFotPreA49.setBounds(880, 391, 40, 40);

        txtPreA49.setEditable(false);
        txtPreA49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA49.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA49);
        txtPreA49.setBounds(704, 397, 109, 28);

        jtaAnoA49.setEditable(false);
        jtaAnoA49.setColumns(20);
        jtaAnoA49.setLineWrap(true);
        jtaAnoA49.setRows(5);
        jspAnoPreA49.setViewportView(jtaAnoA49);

        jpaAba2.add(jspAnoPreA49);
        jspAnoPreA49.setBounds(750, 428, 40, 10);

        jtaAnoA50.setEditable(false);
        jtaAnoA50.setColumns(20);
        jtaAnoA50.setLineWrap(true);
        jtaAnoA50.setRows(5);
        jspAnoPreA50.setViewportView(jtaAnoA50);

        jpaAba2.add(jspAnoPreA50);
        jspAnoPreA50.setBounds(750, 481, 40, 10);

        txtPreA50.setEditable(false);
        txtPreA50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA50.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA50);
        txtPreA50.setBounds(704, 450, 109, 28);

        jtbAnoPreA50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA50ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA50);
        jtbAnoPreA50.setBounds(824, 444, 40, 40);

        btnFotPreA50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA50ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA50);
        btnFotPreA50.setBounds(880, 444, 40, 40);

        txtPreA51.setEditable(false);
        txtPreA51.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA51);
        txtPreA51.setBounds(704, 504, 109, 28);

        jtbAnoPreA51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA51ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA51);
        jtbAnoPreA51.setBounds(824, 498, 40, 40);

        btnFotPreA51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA51ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA51);
        btnFotPreA51.setBounds(880, 498, 40, 40);

        jtaAnoA51.setEditable(false);
        jtaAnoA51.setColumns(20);
        jtaAnoA51.setLineWrap(true);
        jtaAnoA51.setRows(5);
        jspAnoPreA51.setViewportView(jtaAnoA51);

        jpaAba2.add(jspAnoPreA51);
        jspAnoPreA51.setBounds(750, 535, 40, 10);

        txtPreA52.setEditable(false);
        txtPreA52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA52.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA52);
        txtPreA52.setBounds(704, 556, 109, 28);

        jtbAnoPreA52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA52ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA52);
        jtbAnoPreA52.setBounds(824, 550, 40, 40);

        btnFotPreA52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA52ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA52);
        btnFotPreA52.setBounds(880, 550, 40, 40);

        jtaAnoA52.setEditable(false);
        jtaAnoA52.setColumns(20);
        jtaAnoA52.setLineWrap(true);
        jtaAnoA52.setRows(5);
        jspAnoPreA52.setViewportView(jtaAnoA52);

        jpaAba2.add(jspAnoPreA52);
        jspAnoPreA52.setBounds(750, 587, 40, 10);

        txtPreA53.setEditable(false);
        txtPreA53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA53);
        txtPreA53.setBounds(704, 610, 109, 28);

        jtbAnoPreA53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA53ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA53);
        jtbAnoPreA53.setBounds(824, 603, 40, 40);

        btnFotPreA53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA53ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA53);
        btnFotPreA53.setBounds(880, 603, 40, 40);

        jtaAnoA53.setEditable(false);
        jtaAnoA53.setColumns(20);
        jtaAnoA53.setLineWrap(true);
        jtaAnoA53.setRows(5);
        jspAnoPreA53.setViewportView(jtaAnoA53);

        jpaAba2.add(jspAnoPreA53);
        jspAnoPreA53.setBounds(750, 640, 40, 10);

        txtPreA54.setEditable(false);
        txtPreA54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA54.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA54);
        txtPreA54.setBounds(704, 660, 109, 28);

        jtbAnoPreA54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA54ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA54);
        jtbAnoPreA54.setBounds(824, 656, 40, 40);

        btnFotPreA54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA54ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA54);
        btnFotPreA54.setBounds(880, 656, 40, 40);

        jtaAnoA54.setEditable(false);
        jtaAnoA54.setColumns(20);
        jtaAnoA54.setLineWrap(true);
        jtaAnoA54.setRows(5);
        jspAnoPreA54.setViewportView(jtaAnoA54);

        jpaAba2.add(jspAnoPreA54);
        jspAnoPreA54.setBounds(750, 693, 40, 10);

        txtPreA55.setEditable(false);
        txtPreA55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA55.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA55);
        txtPreA55.setBounds(704, 716, 109, 28);

        jtbAnoPreA55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA55ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA55);
        jtbAnoPreA55.setBounds(824, 709, 40, 40);

        btnFotPreA55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA55ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA55);
        btnFotPreA55.setBounds(880, 709, 40, 40);

        jtaAnoA55.setEditable(false);
        jtaAnoA55.setColumns(20);
        jtaAnoA55.setLineWrap(true);
        jtaAnoA55.setRows(5);
        jspAnoPreA55.setViewportView(jtaAnoA55);

        jpaAba2.add(jspAnoPreA55);
        jspAnoPreA55.setBounds(750, 746, 40, 10);

        txtPreA56.setEditable(false);
        txtPreA56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA56.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA56);
        txtPreA56.setBounds(704, 770, 109, 28);

        jtbAnoPreA56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA56ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA56);
        jtbAnoPreA56.setBounds(824, 763, 40, 40);

        btnFotPreA56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA56ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA56);
        btnFotPreA56.setBounds(880, 763, 40, 40);

        jtaAnoA56.setEditable(false);
        jtaAnoA56.setColumns(20);
        jtaAnoA56.setLineWrap(true);
        jtaAnoA56.setRows(5);
        jspAnoPreA56.setViewportView(jtaAnoA56);

        jpaAba2.add(jspAnoPreA56);
        jspAnoPreA56.setBounds(750, 800, 40, 10);

        txtPreA57.setEditable(false);
        txtPreA57.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA57.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA57);
        txtPreA57.setBounds(704, 822, 109, 28);

        jtaAnoA57.setEditable(false);
        jtaAnoA57.setColumns(20);
        jtaAnoA57.setLineWrap(true);
        jtaAnoA57.setRows(5);
        jspAnoPreA57.setViewportView(jtaAnoA57);

        jpaAba2.add(jspAnoPreA57);
        jspAnoPreA57.setBounds(750, 853, 40, 10);

        jtbAnoPreA57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA57ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA57);
        jtbAnoPreA57.setBounds(824, 816, 40, 40);

        btnFotPreA57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA57ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA57);
        btnFotPreA57.setBounds(880, 816, 40, 40);

        txtPreA58.setEditable(false);
        txtPreA58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA58.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA58);
        txtPreA58.setBounds(704, 875, 109, 28);

        jtbAnoPreA58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA58ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA58);
        jtbAnoPreA58.setBounds(824, 868, 40, 40);

        btnFotPreA58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA58ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA58);
        btnFotPreA58.setBounds(880, 868, 40, 40);

        jtaAnoA58.setEditable(false);
        jtaAnoA58.setColumns(20);
        jtaAnoA58.setLineWrap(true);
        jtaAnoA58.setRows(5);
        jspAnoPreA58.setViewportView(jtaAnoA58);

        jpaAba2.add(jspAnoPreA58);
        jspAnoPreA58.setBounds(750, 906, 40, 10);

        txtPreA59.setEditable(false);
        txtPreA59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA59.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA59);
        txtPreA59.setBounds(704, 928, 109, 28);

        jtbAnoPreA59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA59ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA59);
        jtbAnoPreA59.setBounds(824, 922, 40, 40);

        btnFotPreA59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA59ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA59);
        btnFotPreA59.setBounds(880, 922, 40, 40);

        jtaAnoA59.setEditable(false);
        jtaAnoA59.setColumns(20);
        jtaAnoA59.setLineWrap(true);
        jtaAnoA59.setRows(5);
        jspAnoPreA59.setViewportView(jtaAnoA59);

        jpaAba2.add(jspAnoPreA59);
        jspAnoPreA59.setBounds(750, 959, 40, 10);

        txtPreA60.setEditable(false);
        txtPreA60.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA60.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA60);
        txtPreA60.setBounds(704, 980, 109, 28);

        jtbAnoPreA60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA60ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA60);
        jtbAnoPreA60.setBounds(824, 974, 40, 40);

        btnFotPreA60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA60ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA60);
        btnFotPreA60.setBounds(880, 974, 40, 40);

        jtaAnoA60.setEditable(false);
        jtaAnoA60.setColumns(20);
        jtaAnoA60.setLineWrap(true);
        jtaAnoA60.setRows(5);
        jspAnoPreA60.setViewportView(jtaAnoA60);

        jpaAba2.add(jspAnoPreA60);
        jspAnoPreA60.setBounds(750, 1012, 40, 10);

        txtPreA61.setEditable(false);
        txtPreA61.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA61.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA61);
        txtPreA61.setBounds(704, 1034, 109, 28);

        jtbAnoPreA61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA61ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA61);
        jtbAnoPreA61.setBounds(824, 1028, 40, 40);

        btnFotPreA61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA61ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA61);
        btnFotPreA61.setBounds(880, 1028, 40, 40);

        jtaAnoA61.setEditable(false);
        jtaAnoA61.setColumns(20);
        jtaAnoA61.setLineWrap(true);
        jtaAnoA61.setRows(5);
        jspAnoPreA61.setViewportView(jtaAnoA61);

        jpaAba2.add(jspAnoPreA61);
        jspAnoPreA61.setBounds(750, 1065, 40, 10);

        txtPreA62.setEditable(false);
        txtPreA62.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA62.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA62);
        txtPreA62.setBounds(704, 1086, 109, 28);

        jtbAnoPreA62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA62ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA62);
        jtbAnoPreA62.setBounds(824, 1080, 40, 40);

        btnFotPreA62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA62ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA62);
        btnFotPreA62.setBounds(880, 1080, 40, 40);

        jtaAnoA62.setEditable(false);
        jtaAnoA62.setColumns(20);
        jtaAnoA62.setLineWrap(true);
        jtaAnoA62.setRows(5);
        jspAnoPreA62.setViewportView(jtaAnoA62);

        jpaAba2.add(jspAnoPreA62);
        jspAnoPreA62.setBounds(750, 1118, 40, 10);

        txtPreA63.setEditable(false);
        txtPreA63.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA63.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA63);
        txtPreA63.setBounds(704, 1140, 109, 28);

        jtbAnoPreA63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA63ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA63);
        jtbAnoPreA63.setBounds(824, 1133, 40, 40);

        btnFotPreA63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA63ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA63);
        btnFotPreA63.setBounds(880, 1133, 40, 40);

        jtaAnoA63.setEditable(false);
        jtaAnoA63.setColumns(20);
        jtaAnoA63.setLineWrap(true);
        jtaAnoA63.setRows(5);
        jspAnoPreA63.setViewportView(jtaAnoA63);

        jpaAba2.add(jspAnoPreA63);
        jspAnoPreA63.setBounds(750, 1171, 40, 10);

        txtPreA64.setEditable(false);
        txtPreA64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA64.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA64);
        txtPreA64.setBounds(704, 1192, 109, 28);

        jtbAnoPreA64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA64ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA64);
        jtbAnoPreA64.setBounds(824, 1186, 40, 40);

        btnFotPreA64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA64ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA64);
        btnFotPreA64.setBounds(880, 1186, 40, 40);

        jtaAnoA64.setEditable(false);
        jtaAnoA64.setColumns(20);
        jtaAnoA64.setLineWrap(true);
        jtaAnoA64.setRows(5);
        jspAnoPreA64.setViewportView(jtaAnoA64);

        jpaAba2.add(jspAnoPreA64);
        jspAnoPreA64.setBounds(750, 1223, 40, 10);

        jtbAnoPreA65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA65ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA65);
        jtbAnoPreA65.setBounds(824, 1240, 40, 40);

        btnFotPreA65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA65ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA65);
        btnFotPreA65.setBounds(880, 1240, 40, 40);

        txtPreA65.setEditable(false);
        txtPreA65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA65.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA65);
        txtPreA65.setBounds(704, 1246, 109, 28);

        jtaAnoA65.setEditable(false);
        jtaAnoA65.setColumns(20);
        jtaAnoA65.setLineWrap(true);
        jtaAnoA65.setRows(5);
        jspAnoPreA65.setViewportView(jtaAnoA65);

        jpaAba2.add(jspAnoPreA65);
        jspAnoPreA65.setBounds(750, 1276, 40, 10);

        jtaAnoA66.setEditable(false);
        jtaAnoA66.setColumns(20);
        jtaAnoA66.setLineWrap(true);
        jtaAnoA66.setRows(5);
        jspAnoPreA66.setViewportView(jtaAnoA66);

        jpaAba2.add(jspAnoPreA66);
        jspAnoPreA66.setBounds(750, 1329, 40, 10);

        txtPreA66.setEditable(false);
        txtPreA66.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA66.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA66);
        txtPreA66.setBounds(704, 1298, 109, 28);

        jtbAnoPreA66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA66ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA66);
        jtbAnoPreA66.setBounds(824, 1292, 40, 40);

        btnFotPreA66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA66ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA66);
        btnFotPreA66.setBounds(880, 1292, 40, 40);

        txtPreA67.setEditable(false);
        txtPreA67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA67.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA67);
        txtPreA67.setBounds(704, 1352, 109, 28);

        jtbAnoPreA67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA67ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA67);
        jtbAnoPreA67.setBounds(824, 1345, 40, 40);

        btnFotPreA67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA67ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA67);
        btnFotPreA67.setBounds(880, 1345, 40, 40);

        jtaAnoA67.setEditable(false);
        jtaAnoA67.setColumns(20);
        jtaAnoA67.setLineWrap(true);
        jtaAnoA67.setRows(5);
        jspAnoPreA67.setViewportView(jtaAnoA67);

        jpaAba2.add(jspAnoPreA67);
        jspAnoPreA67.setBounds(750, 1383, 40, 10);

        txtPreA68.setEditable(false);
        txtPreA68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA68.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA68);
        txtPreA68.setBounds(704, 1406, 109, 28);

        jtbAnoPreA68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA68ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA68);
        jtbAnoPreA68.setBounds(824, 1398, 40, 40);

        btnFotPreA68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA68ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA68);
        btnFotPreA68.setBounds(880, 1398, 40, 40);

        jtaAnoA68.setEditable(false);
        jtaAnoA68.setColumns(20);
        jtaAnoA68.setLineWrap(true);
        jtaAnoA68.setRows(5);
        jspAnoPreA68.setViewportView(jtaAnoA68);

        jpaAba2.add(jspAnoPreA68);
        jspAnoPreA68.setBounds(750, 1436, 40, 10);

        txtPreA69.setEditable(false);
        txtPreA69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA69.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA69);
        txtPreA69.setBounds(704, 1458, 109, 28);

        jtbAnoPreA69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA69ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA69);
        jtbAnoPreA69.setBounds(824, 1452, 40, 40);

        btnFotPreA69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA69ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA69);
        btnFotPreA69.setBounds(880, 1452, 40, 40);

        jtaAnoA69.setEditable(false);
        jtaAnoA69.setColumns(20);
        jtaAnoA69.setLineWrap(true);
        jtaAnoA69.setRows(5);
        jspAnoPreA69.setViewportView(jtaAnoA69);

        jpaAba2.add(jspAnoPreA69);
        jspAnoPreA69.setBounds(750, 1489, 40, 10);

        txtPreA70.setEditable(false);
        txtPreA70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA70.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA70);
        txtPreA70.setBounds(704, 1510, 109, 28);

        jtbAnoPreA70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA70ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA70);
        jtbAnoPreA70.setBounds(824, 1505, 40, 40);

        btnFotPreA70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA70ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA70);
        btnFotPreA70.setBounds(880, 1505, 40, 40);

        jtaAnoA70.setEditable(false);
        jtaAnoA70.setColumns(20);
        jtaAnoA70.setLineWrap(true);
        jtaAnoA70.setRows(5);
        jspAnoPreA70.setViewportView(jtaAnoA70);

        jpaAba2.add(jspAnoPreA70);
        jspAnoPreA70.setBounds(750, 1542, 40, 10);

        txtPreA71.setEditable(false);
        txtPreA71.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA71.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA71);
        txtPreA71.setBounds(704, 1564, 109, 28);

        jtbAnoPreA71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA71ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA71);
        jtbAnoPreA71.setBounds(824, 1557, 40, 40);

        btnFotPreA71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA71ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA71);
        btnFotPreA71.setBounds(880, 1557, 40, 40);

        jtaAnoA71.setEditable(false);
        jtaAnoA71.setColumns(20);
        jtaAnoA71.setLineWrap(true);
        jtaAnoA71.setRows(5);
        jspAnoPreA71.setViewportView(jtaAnoA71);

        jpaAba2.add(jspAnoPreA71);
        jspAnoPreA71.setBounds(750, 1595, 40, 10);

        txtPreA72.setEditable(false);
        txtPreA72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA72.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA72);
        txtPreA72.setBounds(704, 1616, 109, 28);

        jtbAnoPreA72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA72ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA72);
        jtbAnoPreA72.setBounds(824, 1610, 40, 40);

        btnFotPreA72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA72ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA72);
        btnFotPreA72.setBounds(880, 1610, 40, 40);

        jtaAnoA72.setEditable(false);
        jtaAnoA72.setColumns(20);
        jtaAnoA72.setLineWrap(true);
        jtaAnoA72.setRows(5);
        jspAnoPreA72.setViewportView(jtaAnoA72);

        jpaAba2.add(jspAnoPreA72);
        jspAnoPreA72.setBounds(750, 1648, 40, 10);

        txtPreA73.setEditable(false);
        txtPreA73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA73.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA73);
        txtPreA73.setBounds(704, 1670, 109, 28);

        jtaAnoA73.setEditable(false);
        jtaAnoA73.setColumns(20);
        jtaAnoA73.setLineWrap(true);
        jtaAnoA73.setRows(5);
        jspAnoPreA73.setViewportView(jtaAnoA73);

        jpaAba2.add(jspAnoPreA73);
        jspAnoPreA73.setBounds(750, 1701, 40, 10);

        jtbAnoPreA73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA73ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA73);
        jtbAnoPreA73.setBounds(824, 1663, 40, 40);

        btnFotPreA73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA73ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA73);
        btnFotPreA73.setBounds(880, 1663, 40, 40);

        txtPreA74.setEditable(false);
        txtPreA74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA74.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA74);
        txtPreA74.setBounds(704, 1723, 109, 28);

        jtbAnoPreA74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA74ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA74);
        jtbAnoPreA74.setBounds(824, 1716, 40, 40);

        btnFotPreA74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA74ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA74);
        btnFotPreA74.setBounds(880, 1716, 40, 40);

        jtaAnoA74.setEditable(false);
        jtaAnoA74.setColumns(20);
        jtaAnoA74.setLineWrap(true);
        jtaAnoA74.setRows(5);
        jspAnoPreA74.setViewportView(jtaAnoA74);

        jpaAba2.add(jspAnoPreA74);
        jspAnoPreA74.setBounds(750, 1754, 40, 10);

        txtPreA75.setEditable(false);
        txtPreA75.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA75.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA75);
        txtPreA75.setBounds(704, 1776, 109, 28);

        jtbAnoPreA75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA75ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA75);
        jtbAnoPreA75.setBounds(824, 1770, 40, 40);

        btnFotPreA75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA75ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA75);
        btnFotPreA75.setBounds(880, 1770, 40, 40);

        jtaAnoA75.setEditable(false);
        jtaAnoA75.setColumns(20);
        jtaAnoA75.setLineWrap(true);
        jtaAnoA75.setRows(5);
        jspAnoPreA75.setViewportView(jtaAnoA75);

        jpaAba2.add(jspAnoPreA75);
        jspAnoPreA75.setBounds(750, 1807, 40, 10);

        txtPreA76.setEditable(false);
        txtPreA76.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA76.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA76);
        txtPreA76.setBounds(704, 1828, 109, 28);

        jtbAnoPreA76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA76ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA76);
        jtbAnoPreA76.setBounds(824, 1823, 40, 40);

        btnFotPreA76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA76ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA76);
        btnFotPreA76.setBounds(880, 1823, 40, 40);

        jtaAnoA76.setEditable(false);
        jtaAnoA76.setColumns(20);
        jtaAnoA76.setLineWrap(true);
        jtaAnoA76.setRows(5);
        jspAnoPreA76.setViewportView(jtaAnoA76);

        jpaAba2.add(jspAnoPreA76);
        jspAnoPreA76.setBounds(750, 1860, 40, 10);

        txtPreA77.setEditable(false);
        txtPreA77.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA77.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA77);
        txtPreA77.setBounds(704, 1882, 109, 28);

        jtbAnoPreA77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA77ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA77);
        jtbAnoPreA77.setBounds(824, 1876, 40, 40);

        btnFotPreA77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA77ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA77);
        btnFotPreA77.setBounds(880, 1876, 40, 40);

        jtaAnoA77.setEditable(false);
        jtaAnoA77.setColumns(20);
        jtaAnoA77.setLineWrap(true);
        jtaAnoA77.setRows(5);
        jspAnoPreA77.setViewportView(jtaAnoA77);

        jpaAba2.add(jspAnoPreA77);
        jspAnoPreA77.setBounds(750, 1913, 40, 10);

        txtPreA78.setEditable(false);
        txtPreA78.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA78.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA78);
        txtPreA78.setBounds(704, 1934, 109, 28);

        jtbAnoPreA78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA78ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA78);
        jtbAnoPreA78.setBounds(824, 1928, 40, 40);

        btnFotPreA78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA78ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA78);
        btnFotPreA78.setBounds(880, 1928, 40, 40);

        jtaAnoA78.setEditable(false);
        jtaAnoA78.setColumns(20);
        jtaAnoA78.setLineWrap(true);
        jtaAnoA78.setRows(5);
        jspAnoPreA78.setViewportView(jtaAnoA78);

        jpaAba2.add(jspAnoPreA78);
        jspAnoPreA78.setBounds(750, 1966, 40, 10);

        txtPreA79.setEditable(false);
        txtPreA79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA79.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA79);
        txtPreA79.setBounds(704, 1988, 109, 28);

        jtbAnoPreA79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA79ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA79);
        jtbAnoPreA79.setBounds(824, 1982, 40, 40);

        btnFotPreA79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA79ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA79);
        btnFotPreA79.setBounds(880, 1982, 40, 40);

        jtaAnoA79.setEditable(false);
        jtaAnoA79.setColumns(20);
        jtaAnoA79.setLineWrap(true);
        jtaAnoA79.setRows(5);
        jspAnoPreA79.setViewportView(jtaAnoA79);

        jpaAba2.add(jspAnoPreA79);
        jspAnoPreA79.setBounds(750, 2019, 40, 10);

        txtPreA80.setEditable(false);
        txtPreA80.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA80.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA80);
        txtPreA80.setBounds(704, 2040, 109, 28);

        jtbAnoPreA80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA80ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA80);
        jtbAnoPreA80.setBounds(824, 2034, 40, 40);

        btnFotPreA80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA80ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA80);
        btnFotPreA80.setBounds(880, 2034, 40, 40);

        jtaAnoA80.setEditable(false);
        jtaAnoA80.setColumns(20);
        jtaAnoA80.setLineWrap(true);
        jtaAnoA80.setRows(5);
        jspAnoPreA80.setViewportView(jtaAnoA80);

        jpaAba2.add(jspAnoPreA80);
        jspAnoPreA80.setBounds(750, 2072, 40, 10);

        jtbAnoPreA81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA81ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA81);
        jtbAnoPreA81.setBounds(824, 2088, 40, 40);

        btnFotPreA81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA81ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA81);
        btnFotPreA81.setBounds(880, 2088, 40, 40);

        jtaAnoA81.setEditable(false);
        jtaAnoA81.setColumns(20);
        jtaAnoA81.setLineWrap(true);
        jtaAnoA81.setRows(5);
        jspAnoPreA81.setViewportView(jtaAnoA81);

        jpaAba2.add(jspAnoPreA81);
        jspAnoPreA81.setBounds(750, 2125, 40, 10);

        jtaAnoA82.setEditable(false);
        jtaAnoA82.setColumns(20);
        jtaAnoA82.setLineWrap(true);
        jtaAnoA82.setRows(5);
        jspAnoPreA82.setViewportView(jtaAnoA82);

        jpaAba2.add(jspAnoPreA82);
        jspAnoPreA82.setBounds(750, 2178, 40, 10);

        txtPreA81.setEditable(false);
        txtPreA81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA81.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA81);
        txtPreA81.setBounds(704, 2094, 109, 28);

        txtPreA82.setEditable(false);
        txtPreA82.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA82.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA82);
        txtPreA82.setBounds(704, 2146, 109, 28);

        jtbAnoPreA82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA82ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA82);
        jtbAnoPreA82.setBounds(824, 2140, 40, 40);

        btnFotPreA82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA82ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA82);
        btnFotPreA82.setBounds(880, 2140, 40, 40);

        txtPreA83.setEditable(false);
        txtPreA83.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA83.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA83);
        txtPreA83.setBounds(704, 2199, 109, 28);

        jtbAnoPreA83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA83ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA83);
        jtbAnoPreA83.setBounds(824, 2193, 40, 40);

        btnFotPreA83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA83ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA83);
        btnFotPreA83.setBounds(880, 2193, 40, 40);

        jtaAnoA83.setEditable(false);
        jtaAnoA83.setColumns(20);
        jtaAnoA83.setLineWrap(true);
        jtaAnoA83.setRows(5);
        jspAnoPreA83.setViewportView(jtaAnoA83);

        jpaAba2.add(jspAnoPreA83);
        jspAnoPreA83.setBounds(750, 2231, 40, 10);

        txtPreA84.setEditable(false);
        txtPreA84.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA84.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA84);
        txtPreA84.setBounds(704, 2252, 109, 28);

        jtbAnoPreA84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA84ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA84);
        jtbAnoPreA84.setBounds(824, 2246, 40, 40);

        btnFotPreA84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA84ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA84);
        btnFotPreA84.setBounds(880, 2246, 40, 40);

        jtaAnoA84.setEditable(false);
        jtaAnoA84.setColumns(20);
        jtaAnoA84.setLineWrap(true);
        jtaAnoA84.setRows(5);
        jspAnoPreA84.setViewportView(jtaAnoA84);

        jpaAba2.add(jspAnoPreA84);
        jspAnoPreA84.setBounds(750, 2284, 40, 10);

        txtPreA85.setEditable(false);
        txtPreA85.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA85);
        txtPreA85.setBounds(704, 2306, 109, 28);

        jtbAnoPreA85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA85ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA85);
        jtbAnoPreA85.setBounds(824, 2300, 40, 40);

        btnFotPreA85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA85ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA85);
        btnFotPreA85.setBounds(880, 2300, 40, 40);

        jtaAnoA85.setEditable(false);
        jtaAnoA85.setColumns(20);
        jtaAnoA85.setLineWrap(true);
        jtaAnoA85.setRows(5);
        jspAnoPreA85.setViewportView(jtaAnoA85);

        jpaAba2.add(jspAnoPreA85);
        jspAnoPreA85.setBounds(750, 2337, 40, 10);

        txtPreA86.setEditable(false);
        txtPreA86.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA86.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba2.add(txtPreA86);
        txtPreA86.setBounds(704, 2358, 109, 28);

        jtbAnoPreA86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA86ActionPerformed(evt);
            }
        });
        jpaAba2.add(jtbAnoPreA86);
        jtbAnoPreA86.setBounds(824, 2352, 40, 40);

        btnFotPreA86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA86ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnFotPreA86);
        btnFotPreA86.setBounds(880, 2352, 40, 40);

        jtaAnoA86.setEditable(false);
        jtaAnoA86.setColumns(20);
        jtaAnoA86.setLineWrap(true);
        jtaAnoA86.setRows(5);
        jspAnoPreA86.setViewportView(jtaAnoA86);

        jpaAba2.add(jspAnoPreA86);
        jspAnoPreA86.setBounds(750, 2390, 40, 10);

        btnImpPre1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre1.setToolTipText("Imprimir Documento");
        btnImpPre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPre1ActionPerformed(evt);
            }
        });
        jpaAba2.add(btnImpPre1);
        btnImpPre1.setBounds(822, 2511, 64, 64);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Elétrica Trimestral.png"))); // NOI18N
        jpaAba2.add(jLabel19);
        jLabel19.setBounds(-12, 0, 1000, 2646);

        jScrollPane6.setViewportView(jpaAba2);

        jtpAbas.addTab("Eletrica Trimestral", jScrollPane6);

        jpaAba1.setPreferredSize(new java.awt.Dimension(960, 3152));
        jpaAba1.setLayout(null);

        txtIdPre.setEditable(false);
        txtIdPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtIdPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtIdPre);
        txtIdPre.setBounds(58, 182, 100, 28);

        txtDatIni.setEditable(false);
        txtDatIni.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtDatIni);
        txtDatIni.setBounds(188, 182, 170, 28);

        txtDatFin.setEditable(false);
        txtDatFin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatFin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtDatFin);
        txtDatFin.setBounds(388, 182, 170, 28);

        txtTemPre.setEditable(false);
        txtTemPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTemPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtTemPre);
        txtTemPre.setBounds(588, 182, 170, 28);

        txtDatPre.setEditable(false);
        txtDatPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDatPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtDatPre);
        txtDatPre.setBounds(788, 182, 140, 28);

        txtNomTec.setEditable(false);
        txtNomTec.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtNomTec);
        txtNomTec.setBounds(58, 243, 180, 28);

        jScrollPane1.setEnabled(false);

        jtaEquPre.setEditable(false);
        jtaEquPre.setColumns(20);
        jtaEquPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaEquPre.setLineWrap(true);
        jtaEquPre.setRows(5);
        jScrollPane1.setViewportView(jtaEquPre);

        jpaAba1.add(jScrollPane1);
        jScrollPane1.setBounds(477, 238, 184, 38);

        jScrollPane4.setEnabled(false);

        jtaSetPre.setEditable(false);
        jtaSetPre.setColumns(20);
        jtaSetPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtaSetPre.setLineWrap(true);
        jtaSetPre.setRows(5);
        jScrollPane4.setViewportView(jtaSetPre);

        jpaAba1.add(jScrollPane4);
        jScrollPane4.setBounds(266, 238, 184, 38);

        txtCodEqu.setEditable(false);
        txtCodEqu.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodEqu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtCodEqu);
        txtCodEqu.setBounds(688, 243, 100, 28);

        txtStaPre.setEditable(false);
        txtStaPre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtStaPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtStaPre);
        txtStaPre.setBounds(818, 243, 110, 28);

        jtbAnoPreA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA1ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA1);
        jtbAnoPreA1.setBounds(824, 391, 40, 40);

        btnFotPreA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA1ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA1);
        btnFotPreA1.setBounds(880, 391, 40, 40);

        jtaAnoA1.setEditable(false);
        jtaAnoA1.setColumns(20);
        jtaAnoA1.setLineWrap(true);
        jtaAnoA1.setRows(5);
        jspAnoPreA1.setViewportView(jtaAnoA1);

        jpaAba1.add(jspAnoPreA1);
        jspAnoPreA1.setBounds(750, 428, 40, 10);

        txtPreA1.setEditable(false);
        txtPreA1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA1);
        txtPreA1.setBounds(704, 397, 109, 28);

        jtaAnoA2.setEditable(false);
        jtaAnoA2.setColumns(20);
        jtaAnoA2.setLineWrap(true);
        jtaAnoA2.setRows(5);
        jspAnoPreA2.setViewportView(jtaAnoA2);

        jpaAba1.add(jspAnoPreA2);
        jspAnoPreA2.setBounds(750, 481, 40, 10);

        txtPreA2.setEditable(false);
        txtPreA2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA2);
        txtPreA2.setBounds(704, 450, 109, 28);

        jtbAnoPreA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA2ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA2);
        jtbAnoPreA2.setBounds(824, 444, 40, 40);

        btnFotPreA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA2ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA2);
        btnFotPreA2.setBounds(880, 444, 40, 40);

        txtPreA3.setEditable(false);
        txtPreA3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA3);
        txtPreA3.setBounds(704, 504, 109, 28);

        jtbAnoPreA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA3ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA3);
        jtbAnoPreA3.setBounds(824, 498, 40, 40);

        btnFotPreA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA3ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA3);
        btnFotPreA3.setBounds(880, 498, 40, 40);

        jtaAnoA3.setEditable(false);
        jtaAnoA3.setColumns(20);
        jtaAnoA3.setLineWrap(true);
        jtaAnoA3.setRows(5);
        jspAnoPreA3.setViewportView(jtaAnoA3);

        jpaAba1.add(jspAnoPreA3);
        jspAnoPreA3.setBounds(750, 535, 40, 10);

        txtPreA4.setEditable(false);
        txtPreA4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA4);
        txtPreA4.setBounds(704, 556, 109, 28);

        jtbAnoPreA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA4ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA4);
        jtbAnoPreA4.setBounds(824, 550, 40, 40);

        btnFotPreA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA4ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA4);
        btnFotPreA4.setBounds(880, 550, 40, 40);

        jtaAnoA4.setEditable(false);
        jtaAnoA4.setColumns(20);
        jtaAnoA4.setLineWrap(true);
        jtaAnoA4.setRows(5);
        jspAnoPreA4.setViewportView(jtaAnoA4);

        jpaAba1.add(jspAnoPreA4);
        jspAnoPreA4.setBounds(750, 587, 40, 10);

        txtPreA5.setEditable(false);
        txtPreA5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA5);
        txtPreA5.setBounds(704, 610, 109, 28);

        jtbAnoPreA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA5ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA5);
        jtbAnoPreA5.setBounds(824, 603, 40, 40);

        btnFotPreA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA5ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA5);
        btnFotPreA5.setBounds(880, 603, 40, 40);

        jtaAnoA5.setEditable(false);
        jtaAnoA5.setColumns(20);
        jtaAnoA5.setLineWrap(true);
        jtaAnoA5.setRows(5);
        jspAnoPreA5.setViewportView(jtaAnoA5);

        jpaAba1.add(jspAnoPreA5);
        jspAnoPreA5.setBounds(750, 640, 40, 10);

        txtPreA6.setEditable(false);
        txtPreA6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA6);
        txtPreA6.setBounds(704, 660, 109, 28);

        jtbAnoPreA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA6ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA6);
        jtbAnoPreA6.setBounds(824, 656, 40, 40);

        btnFotPreA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA6ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA6);
        btnFotPreA6.setBounds(880, 656, 40, 40);

        jtaAnoA6.setEditable(false);
        jtaAnoA6.setColumns(20);
        jtaAnoA6.setLineWrap(true);
        jtaAnoA6.setRows(5);
        jspAnoPreA6.setViewportView(jtaAnoA6);

        jpaAba1.add(jspAnoPreA6);
        jspAnoPreA6.setBounds(750, 693, 40, 10);

        txtPreA7.setEditable(false);
        txtPreA7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA7);
        txtPreA7.setBounds(704, 716, 109, 28);

        jtbAnoPreA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA7ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA7);
        jtbAnoPreA7.setBounds(824, 709, 40, 40);

        btnFotPreA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA7ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA7);
        btnFotPreA7.setBounds(880, 709, 40, 40);

        jtaAnoA7.setEditable(false);
        jtaAnoA7.setColumns(20);
        jtaAnoA7.setLineWrap(true);
        jtaAnoA7.setRows(5);
        jspAnoPreA7.setViewportView(jtaAnoA7);

        jpaAba1.add(jspAnoPreA7);
        jspAnoPreA7.setBounds(750, 746, 40, 10);

        txtPreA8.setEditable(false);
        txtPreA8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA8);
        txtPreA8.setBounds(704, 770, 109, 28);

        jtbAnoPreA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA8ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA8);
        jtbAnoPreA8.setBounds(824, 763, 40, 40);

        btnFotPreA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA8ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA8);
        btnFotPreA8.setBounds(880, 763, 40, 40);

        jtaAnoA8.setEditable(false);
        jtaAnoA8.setColumns(20);
        jtaAnoA8.setLineWrap(true);
        jtaAnoA8.setRows(5);
        jspAnoPreA8.setViewportView(jtaAnoA8);

        jpaAba1.add(jspAnoPreA8);
        jspAnoPreA8.setBounds(750, 800, 40, 10);

        txtPreA9.setEditable(false);
        txtPreA9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA9);
        txtPreA9.setBounds(704, 822, 109, 28);

        jtaAnoA9.setEditable(false);
        jtaAnoA9.setColumns(20);
        jtaAnoA9.setLineWrap(true);
        jtaAnoA9.setRows(5);
        jspAnoPreA9.setViewportView(jtaAnoA9);

        jpaAba1.add(jspAnoPreA9);
        jspAnoPreA9.setBounds(750, 853, 40, 10);

        jtbAnoPreA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA9ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA9);
        jtbAnoPreA9.setBounds(824, 816, 40, 40);

        btnFotPreA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA9ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA9);
        btnFotPreA9.setBounds(880, 816, 40, 40);

        txtPreA10.setEditable(false);
        txtPreA10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA10);
        txtPreA10.setBounds(704, 875, 109, 28);

        jtbAnoPreA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA10ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA10);
        jtbAnoPreA10.setBounds(824, 868, 40, 40);

        btnFotPreA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA10ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA10);
        btnFotPreA10.setBounds(880, 868, 40, 40);

        jtaAnoA10.setEditable(false);
        jtaAnoA10.setColumns(20);
        jtaAnoA10.setLineWrap(true);
        jtaAnoA10.setRows(5);
        jspAnoPreA10.setViewportView(jtaAnoA10);

        jpaAba1.add(jspAnoPreA10);
        jspAnoPreA10.setBounds(750, 906, 40, 10);

        txtPreA11.setEditable(false);
        txtPreA11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA11);
        txtPreA11.setBounds(704, 928, 109, 28);

        jtbAnoPreA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA11ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA11);
        jtbAnoPreA11.setBounds(824, 922, 40, 40);

        btnFotPreA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA11ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA11);
        btnFotPreA11.setBounds(880, 922, 40, 40);

        jtaAnoA11.setEditable(false);
        jtaAnoA11.setColumns(20);
        jtaAnoA11.setLineWrap(true);
        jtaAnoA11.setRows(5);
        jspAnoPreA11.setViewportView(jtaAnoA11);

        jpaAba1.add(jspAnoPreA11);
        jspAnoPreA11.setBounds(750, 959, 40, 10);

        txtPreA12.setEditable(false);
        txtPreA12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA12);
        txtPreA12.setBounds(704, 980, 109, 28);

        jtbAnoPreA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA12ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA12);
        jtbAnoPreA12.setBounds(824, 974, 40, 40);

        btnFotPreA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA12ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA12);
        btnFotPreA12.setBounds(880, 974, 40, 40);

        jtaAnoA12.setEditable(false);
        jtaAnoA12.setColumns(20);
        jtaAnoA12.setLineWrap(true);
        jtaAnoA12.setRows(5);
        jspAnoPreA12.setViewportView(jtaAnoA12);

        jpaAba1.add(jspAnoPreA12);
        jspAnoPreA12.setBounds(750, 1012, 40, 10);

        txtPreA13.setEditable(false);
        txtPreA13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA13);
        txtPreA13.setBounds(704, 1034, 109, 28);

        jtbAnoPreA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA13ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA13);
        jtbAnoPreA13.setBounds(824, 1028, 40, 40);

        btnFotPreA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA13ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA13);
        btnFotPreA13.setBounds(880, 1028, 40, 40);

        jtaAnoA13.setEditable(false);
        jtaAnoA13.setColumns(20);
        jtaAnoA13.setLineWrap(true);
        jtaAnoA13.setRows(5);
        jspAnoPreA13.setViewportView(jtaAnoA13);

        jpaAba1.add(jspAnoPreA13);
        jspAnoPreA13.setBounds(750, 1065, 40, 10);

        txtPreA14.setEditable(false);
        txtPreA14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA14);
        txtPreA14.setBounds(704, 1086, 109, 28);

        jtbAnoPreA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA14ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA14);
        jtbAnoPreA14.setBounds(824, 1080, 40, 40);

        btnFotPreA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA14ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA14);
        btnFotPreA14.setBounds(880, 1080, 40, 40);

        jtaAnoA14.setEditable(false);
        jtaAnoA14.setColumns(20);
        jtaAnoA14.setLineWrap(true);
        jtaAnoA14.setRows(5);
        jspAnoPreA14.setViewportView(jtaAnoA14);

        jpaAba1.add(jspAnoPreA14);
        jspAnoPreA14.setBounds(750, 1118, 40, 10);

        txtPreA15.setEditable(false);
        txtPreA15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA15);
        txtPreA15.setBounds(704, 1140, 109, 28);

        jtbAnoPreA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA15ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA15);
        jtbAnoPreA15.setBounds(824, 1133, 40, 40);

        btnFotPreA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA15ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA15);
        btnFotPreA15.setBounds(880, 1133, 40, 40);

        jtaAnoA15.setEditable(false);
        jtaAnoA15.setColumns(20);
        jtaAnoA15.setLineWrap(true);
        jtaAnoA15.setRows(5);
        jspAnoPreA15.setViewportView(jtaAnoA15);

        jpaAba1.add(jspAnoPreA15);
        jspAnoPreA15.setBounds(750, 1171, 40, 10);

        txtPreA16.setEditable(false);
        txtPreA16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA16);
        txtPreA16.setBounds(704, 1192, 109, 28);

        jtbAnoPreA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA16ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA16);
        jtbAnoPreA16.setBounds(824, 1186, 40, 40);

        btnFotPreA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA16ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA16);
        btnFotPreA16.setBounds(880, 1186, 40, 40);

        jtaAnoA16.setEditable(false);
        jtaAnoA16.setColumns(20);
        jtaAnoA16.setLineWrap(true);
        jtaAnoA16.setRows(5);
        jspAnoPreA16.setViewportView(jtaAnoA16);

        jpaAba1.add(jspAnoPreA16);
        jspAnoPreA16.setBounds(750, 1223, 40, 10);

        jtbAnoPreA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA17ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA17);
        jtbAnoPreA17.setBounds(824, 1240, 40, 40);

        btnFotPreA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA17ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA17);
        btnFotPreA17.setBounds(880, 1240, 40, 40);

        txtPreA17.setEditable(false);
        txtPreA17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA17);
        txtPreA17.setBounds(704, 1246, 109, 28);

        jtaAnoA17.setEditable(false);
        jtaAnoA17.setColumns(20);
        jtaAnoA17.setLineWrap(true);
        jtaAnoA17.setRows(5);
        jspAnoPreA17.setViewportView(jtaAnoA17);

        jpaAba1.add(jspAnoPreA17);
        jspAnoPreA17.setBounds(750, 1276, 40, 10);

        jtaAnoA18.setEditable(false);
        jtaAnoA18.setColumns(20);
        jtaAnoA18.setLineWrap(true);
        jtaAnoA18.setRows(5);
        jspAnoPreA18.setViewportView(jtaAnoA18);

        jpaAba1.add(jspAnoPreA18);
        jspAnoPreA18.setBounds(750, 1329, 40, 10);

        txtPreA18.setEditable(false);
        txtPreA18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA18);
        txtPreA18.setBounds(704, 1298, 109, 28);

        jtbAnoPreA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA18ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA18);
        jtbAnoPreA18.setBounds(824, 1292, 40, 40);

        btnFotPreA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA18ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA18);
        btnFotPreA18.setBounds(880, 1292, 40, 40);

        txtPreA19.setEditable(false);
        txtPreA19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA19);
        txtPreA19.setBounds(704, 1352, 109, 28);

        jtbAnoPreA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA19ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA19);
        jtbAnoPreA19.setBounds(824, 1345, 40, 40);

        btnFotPreA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA19ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA19);
        btnFotPreA19.setBounds(880, 1345, 40, 40);

        jtaAnoA19.setEditable(false);
        jtaAnoA19.setColumns(20);
        jtaAnoA19.setLineWrap(true);
        jtaAnoA19.setRows(5);
        jspAnoPreA19.setViewportView(jtaAnoA19);

        jpaAba1.add(jspAnoPreA19);
        jspAnoPreA19.setBounds(750, 1383, 40, 10);

        txtPreA20.setEditable(false);
        txtPreA20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA20);
        txtPreA20.setBounds(704, 1406, 109, 28);

        jtbAnoPreA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA20ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA20);
        jtbAnoPreA20.setBounds(824, 1398, 40, 40);

        btnFotPreA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA20ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA20);
        btnFotPreA20.setBounds(880, 1398, 40, 40);

        jtaAnoA20.setEditable(false);
        jtaAnoA20.setColumns(20);
        jtaAnoA20.setLineWrap(true);
        jtaAnoA20.setRows(5);
        jspAnoPreA20.setViewportView(jtaAnoA20);

        jpaAba1.add(jspAnoPreA20);
        jspAnoPreA20.setBounds(750, 1436, 40, 10);

        txtPreA21.setEditable(false);
        txtPreA21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA21);
        txtPreA21.setBounds(704, 1458, 109, 28);

        jtbAnoPreA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA21ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA21);
        jtbAnoPreA21.setBounds(824, 1452, 40, 40);

        btnFotPreA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA21ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA21);
        btnFotPreA21.setBounds(880, 1452, 40, 40);

        jtaAnoA21.setEditable(false);
        jtaAnoA21.setColumns(20);
        jtaAnoA21.setLineWrap(true);
        jtaAnoA21.setRows(5);
        jspAnoPreA21.setViewportView(jtaAnoA21);

        jpaAba1.add(jspAnoPreA21);
        jspAnoPreA21.setBounds(750, 1489, 40, 10);

        txtPreA22.setEditable(false);
        txtPreA22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA22);
        txtPreA22.setBounds(704, 1510, 109, 28);

        jtbAnoPreA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA22ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA22);
        jtbAnoPreA22.setBounds(824, 1505, 40, 40);

        btnFotPreA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA22ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA22);
        btnFotPreA22.setBounds(880, 1505, 40, 40);

        jtaAnoA22.setEditable(false);
        jtaAnoA22.setColumns(20);
        jtaAnoA22.setLineWrap(true);
        jtaAnoA22.setRows(5);
        jspAnoPreA22.setViewportView(jtaAnoA22);

        jpaAba1.add(jspAnoPreA22);
        jspAnoPreA22.setBounds(750, 1542, 40, 10);

        txtPreA23.setEditable(false);
        txtPreA23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA23);
        txtPreA23.setBounds(704, 1564, 109, 28);

        jtbAnoPreA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA23ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA23);
        jtbAnoPreA23.setBounds(824, 1557, 40, 40);

        btnFotPreA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA23ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA23);
        btnFotPreA23.setBounds(880, 1557, 40, 40);

        jtaAnoA23.setEditable(false);
        jtaAnoA23.setColumns(20);
        jtaAnoA23.setLineWrap(true);
        jtaAnoA23.setRows(5);
        jspAnoPreA23.setViewportView(jtaAnoA23);

        jpaAba1.add(jspAnoPreA23);
        jspAnoPreA23.setBounds(750, 1595, 40, 10);

        txtPreA24.setEditable(false);
        txtPreA24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA24);
        txtPreA24.setBounds(704, 1616, 109, 28);

        jtbAnoPreA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA24ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA24);
        jtbAnoPreA24.setBounds(824, 1610, 40, 40);

        btnFotPreA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA24ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA24);
        btnFotPreA24.setBounds(880, 1610, 40, 40);

        jtaAnoA24.setEditable(false);
        jtaAnoA24.setColumns(20);
        jtaAnoA24.setLineWrap(true);
        jtaAnoA24.setRows(5);
        jspAnoPreA24.setViewportView(jtaAnoA24);

        jpaAba1.add(jspAnoPreA24);
        jspAnoPreA24.setBounds(750, 1648, 40, 10);

        txtPreA25.setEditable(false);
        txtPreA25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA25);
        txtPreA25.setBounds(704, 1670, 109, 28);

        jtaAnoA25.setEditable(false);
        jtaAnoA25.setColumns(20);
        jtaAnoA25.setLineWrap(true);
        jtaAnoA25.setRows(5);
        jspAnoPreA25.setViewportView(jtaAnoA25);

        jpaAba1.add(jspAnoPreA25);
        jspAnoPreA25.setBounds(750, 1701, 40, 10);

        jtbAnoPreA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA25ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA25);
        jtbAnoPreA25.setBounds(824, 1663, 40, 40);

        btnFotPreA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA25ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA25);
        btnFotPreA25.setBounds(880, 1663, 40, 40);

        txtPreA26.setEditable(false);
        txtPreA26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA26);
        txtPreA26.setBounds(704, 1723, 109, 28);

        jtbAnoPreA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA26ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA26);
        jtbAnoPreA26.setBounds(824, 1716, 40, 40);

        btnFotPreA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA26ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA26);
        btnFotPreA26.setBounds(880, 1716, 40, 40);

        jtaAnoA26.setEditable(false);
        jtaAnoA26.setColumns(20);
        jtaAnoA26.setLineWrap(true);
        jtaAnoA26.setRows(5);
        jspAnoPreA26.setViewportView(jtaAnoA26);

        jpaAba1.add(jspAnoPreA26);
        jspAnoPreA26.setBounds(750, 1754, 40, 10);

        txtPreA27.setEditable(false);
        txtPreA27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA27);
        txtPreA27.setBounds(704, 1776, 109, 28);

        jtbAnoPreA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA27ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA27);
        jtbAnoPreA27.setBounds(824, 1770, 40, 40);

        btnFotPreA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA27ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA27);
        btnFotPreA27.setBounds(880, 1770, 40, 40);

        jtaAnoA27.setEditable(false);
        jtaAnoA27.setColumns(20);
        jtaAnoA27.setLineWrap(true);
        jtaAnoA27.setRows(5);
        jspAnoPreA27.setViewportView(jtaAnoA27);

        jpaAba1.add(jspAnoPreA27);
        jspAnoPreA27.setBounds(750, 1807, 40, 10);

        txtPreA28.setEditable(false);
        txtPreA28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA28);
        txtPreA28.setBounds(704, 1828, 109, 28);

        jtbAnoPreA28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA28ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA28);
        jtbAnoPreA28.setBounds(824, 1823, 40, 40);

        btnFotPreA28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA28ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA28);
        btnFotPreA28.setBounds(880, 1823, 40, 40);

        jtaAnoA28.setEditable(false);
        jtaAnoA28.setColumns(20);
        jtaAnoA28.setLineWrap(true);
        jtaAnoA28.setRows(5);
        jspAnoPreA28.setViewportView(jtaAnoA28);

        jpaAba1.add(jspAnoPreA28);
        jspAnoPreA28.setBounds(750, 1860, 40, 10);

        txtPreA29.setEditable(false);
        txtPreA29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA29);
        txtPreA29.setBounds(704, 1882, 109, 28);

        jtbAnoPreA29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA29ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA29);
        jtbAnoPreA29.setBounds(824, 1876, 40, 40);

        btnFotPreA29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA29ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA29);
        btnFotPreA29.setBounds(880, 1876, 40, 40);

        jtaAnoA29.setEditable(false);
        jtaAnoA29.setColumns(20);
        jtaAnoA29.setLineWrap(true);
        jtaAnoA29.setRows(5);
        jspAnoPreA29.setViewportView(jtaAnoA29);

        jpaAba1.add(jspAnoPreA29);
        jspAnoPreA29.setBounds(750, 1913, 40, 10);

        txtPreA30.setEditable(false);
        txtPreA30.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA30);
        txtPreA30.setBounds(704, 1934, 109, 28);

        jtbAnoPreA30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA30ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA30);
        jtbAnoPreA30.setBounds(824, 1928, 40, 40);

        btnFotPreA30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA30ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA30);
        btnFotPreA30.setBounds(880, 1928, 40, 40);

        jtaAnoA30.setEditable(false);
        jtaAnoA30.setColumns(20);
        jtaAnoA30.setLineWrap(true);
        jtaAnoA30.setRows(5);
        jspAnoPreA30.setViewportView(jtaAnoA30);

        jpaAba1.add(jspAnoPreA30);
        jspAnoPreA30.setBounds(750, 1966, 40, 10);

        txtPreA31.setEditable(false);
        txtPreA31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA31);
        txtPreA31.setBounds(704, 1988, 109, 28);

        jtbAnoPreA31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA31ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA31);
        jtbAnoPreA31.setBounds(824, 1982, 40, 40);

        btnFotPreA31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA31ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA31);
        btnFotPreA31.setBounds(880, 1982, 40, 40);

        jtaAnoA31.setEditable(false);
        jtaAnoA31.setColumns(20);
        jtaAnoA31.setLineWrap(true);
        jtaAnoA31.setRows(5);
        jspAnoPreA31.setViewportView(jtaAnoA31);

        jpaAba1.add(jspAnoPreA31);
        jspAnoPreA31.setBounds(750, 2019, 40, 10);

        txtPreA32.setEditable(false);
        txtPreA32.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA32);
        txtPreA32.setBounds(704, 2040, 109, 28);

        jtbAnoPreA32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA32ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA32);
        jtbAnoPreA32.setBounds(824, 2034, 40, 40);

        btnFotPreA32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA32ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA32);
        btnFotPreA32.setBounds(880, 2034, 40, 40);

        jtaAnoA32.setEditable(false);
        jtaAnoA32.setColumns(20);
        jtaAnoA32.setLineWrap(true);
        jtaAnoA32.setRows(5);
        jspAnoPreA32.setViewportView(jtaAnoA32);

        jpaAba1.add(jspAnoPreA32);
        jspAnoPreA32.setBounds(750, 2072, 40, 10);

        jtbAnoPreA33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA33ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA33);
        jtbAnoPreA33.setBounds(824, 2088, 40, 40);

        btnFotPreA33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA33ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA33);
        btnFotPreA33.setBounds(880, 2088, 40, 40);

        txtPreA33.setEditable(false);
        txtPreA33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA33);
        txtPreA33.setBounds(704, 2094, 109, 28);

        jtaAnoA33.setEditable(false);
        jtaAnoA33.setColumns(20);
        jtaAnoA33.setLineWrap(true);
        jtaAnoA33.setRows(5);
        jspAnoPreA33.setViewportView(jtaAnoA33);

        jpaAba1.add(jspAnoPreA33);
        jspAnoPreA33.setBounds(750, 2125, 40, 10);

        jtaAnoA34.setEditable(false);
        jtaAnoA34.setColumns(20);
        jtaAnoA34.setLineWrap(true);
        jtaAnoA34.setRows(5);
        jspAnoPreA34.setViewportView(jtaAnoA34);

        jpaAba1.add(jspAnoPreA34);
        jspAnoPreA34.setBounds(750, 2178, 40, 10);

        txtPreA34.setEditable(false);
        txtPreA34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA34);
        txtPreA34.setBounds(704, 2146, 109, 28);

        jtbAnoPreA34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA34ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA34);
        jtbAnoPreA34.setBounds(824, 2140, 40, 40);

        btnFotPreA34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA34ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA34);
        btnFotPreA34.setBounds(880, 2140, 40, 40);

        txtPreA35.setEditable(false);
        txtPreA35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA35);
        txtPreA35.setBounds(704, 2199, 109, 28);

        jtbAnoPreA35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA35ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA35);
        jtbAnoPreA35.setBounds(824, 2193, 40, 40);

        btnFotPreA35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA35ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA35);
        btnFotPreA35.setBounds(880, 2193, 40, 40);

        jtaAnoA35.setEditable(false);
        jtaAnoA35.setColumns(20);
        jtaAnoA35.setLineWrap(true);
        jtaAnoA35.setRows(5);
        jspAnoPreA35.setViewportView(jtaAnoA35);

        jpaAba1.add(jspAnoPreA35);
        jspAnoPreA35.setBounds(750, 2231, 40, 10);

        txtPreA36.setEditable(false);
        txtPreA36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA36);
        txtPreA36.setBounds(704, 2252, 109, 28);

        jtbAnoPreA36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA36ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA36);
        jtbAnoPreA36.setBounds(824, 2246, 40, 40);

        btnFotPreA36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA36ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA36);
        btnFotPreA36.setBounds(880, 2246, 40, 40);

        jtaAnoA36.setEditable(false);
        jtaAnoA36.setColumns(20);
        jtaAnoA36.setLineWrap(true);
        jtaAnoA36.setRows(5);
        jspAnoPreA36.setViewportView(jtaAnoA36);

        jpaAba1.add(jspAnoPreA36);
        jspAnoPreA36.setBounds(750, 2284, 40, 10);

        txtPreA37.setEditable(false);
        txtPreA37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA37.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA37);
        txtPreA37.setBounds(704, 2306, 109, 28);

        jtbAnoPreA37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA37ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA37);
        jtbAnoPreA37.setBounds(824, 2300, 40, 40);

        btnFotPreA37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA37ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA37);
        btnFotPreA37.setBounds(880, 2300, 40, 40);

        jtaAnoA37.setEditable(false);
        jtaAnoA37.setColumns(20);
        jtaAnoA37.setLineWrap(true);
        jtaAnoA37.setRows(5);
        jspAnoPreA37.setViewportView(jtaAnoA37);

        jpaAba1.add(jspAnoPreA37);
        jspAnoPreA37.setBounds(750, 2337, 40, 10);

        txtPreA38.setEditable(false);
        txtPreA38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA38.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA38);
        txtPreA38.setBounds(704, 2358, 109, 28);

        jtbAnoPreA38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA38ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA38);
        jtbAnoPreA38.setBounds(824, 2352, 40, 40);

        btnFotPreA38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA38ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA38);
        btnFotPreA38.setBounds(880, 2352, 40, 40);

        jtaAnoA38.setEditable(false);
        jtaAnoA38.setColumns(20);
        jtaAnoA38.setLineWrap(true);
        jtaAnoA38.setRows(5);
        jspAnoPreA38.setViewportView(jtaAnoA38);

        jpaAba1.add(jspAnoPreA38);
        jspAnoPreA38.setBounds(750, 2390, 40, 10);

        txtPreA39.setEditable(false);
        txtPreA39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA39.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA39);
        txtPreA39.setBounds(704, 2412, 109, 28);

        jtbAnoPreA39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA39ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA39);
        jtbAnoPreA39.setBounds(824, 2405, 40, 40);

        btnFotPreA39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA39ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA39);
        btnFotPreA39.setBounds(880, 2405, 40, 40);

        jtaAnoA39.setEditable(false);
        jtaAnoA39.setColumns(20);
        jtaAnoA39.setLineWrap(true);
        jtaAnoA39.setRows(5);
        jspAnoPreA39.setViewportView(jtaAnoA39);

        jpaAba1.add(jspAnoPreA39);
        jspAnoPreA39.setBounds(750, 2443, 40, 10);

        txtPreA40.setEditable(false);
        txtPreA40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA40.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA40);
        txtPreA40.setBounds(704, 2464, 109, 28);

        jtbAnoPreA40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA40ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA40);
        jtbAnoPreA40.setBounds(824, 2458, 40, 40);

        btnFotPreA40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA40ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA40);
        btnFotPreA40.setBounds(880, 2458, 40, 40);

        jtaAnoA40.setEditable(false);
        jtaAnoA40.setColumns(20);
        jtaAnoA40.setLineWrap(true);
        jtaAnoA40.setRows(5);
        jspAnoPreA40.setViewportView(jtaAnoA40);

        jpaAba1.add(jspAnoPreA40);
        jspAnoPreA40.setBounds(750, 2496, 40, 10);

        txtPreA41.setEditable(false);
        txtPreA41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA41.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA41);
        txtPreA41.setBounds(704, 2518, 109, 28);

        jtaAnoA41.setEditable(false);
        jtaAnoA41.setColumns(20);
        jtaAnoA41.setLineWrap(true);
        jtaAnoA41.setRows(5);
        jspAnoPreA41.setViewportView(jtaAnoA41);

        jpaAba1.add(jspAnoPreA41);
        jspAnoPreA41.setBounds(750, 2549, 40, 10);

        jtbAnoPreA41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA41ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA41);
        jtbAnoPreA41.setBounds(824, 2511, 40, 40);

        btnFotPreA41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA41ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA41);
        btnFotPreA41.setBounds(880, 2511, 40, 40);

        txtPreA42.setEditable(false);
        txtPreA42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA42);
        txtPreA42.setBounds(704, 2572, 109, 28);

        jtbAnoPreA42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA42ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA42);
        jtbAnoPreA42.setBounds(824, 2564, 40, 40);

        btnFotPreA42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA42ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA42);
        btnFotPreA42.setBounds(880, 2564, 40, 40);

        jtaAnoA42.setEditable(false);
        jtaAnoA42.setColumns(20);
        jtaAnoA42.setLineWrap(true);
        jtaAnoA42.setRows(5);
        jspAnoPreA42.setViewportView(jtaAnoA42);

        jpaAba1.add(jspAnoPreA42);
        jspAnoPreA42.setBounds(750, 2606, 40, 10);

        txtPreA43.setEditable(false);
        txtPreA43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA43);
        txtPreA43.setBounds(704, 2623, 109, 28);

        jtbAnoPreA43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA43ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA43);
        jtbAnoPreA43.setBounds(824, 2617, 40, 40);

        btnFotPreA43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA43ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA43);
        btnFotPreA43.setBounds(880, 2617, 40, 40);

        jtaAnoA43.setEditable(false);
        jtaAnoA43.setColumns(20);
        jtaAnoA43.setLineWrap(true);
        jtaAnoA43.setRows(5);
        jspAnoPreA43.setViewportView(jtaAnoA43);

        jpaAba1.add(jspAnoPreA43);
        jspAnoPreA43.setBounds(750, 2655, 40, 10);

        txtPreA44.setEditable(false);
        txtPreA44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA44.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA44);
        txtPreA44.setBounds(704, 2676, 109, 28);

        jtbAnoPreA44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA44ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA44);
        jtbAnoPreA44.setBounds(824, 2670, 40, 40);

        btnFotPreA44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA44ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA44);
        btnFotPreA44.setBounds(880, 2670, 40, 40);

        jtaAnoA44.setEditable(false);
        jtaAnoA44.setColumns(20);
        jtaAnoA44.setLineWrap(true);
        jtaAnoA44.setRows(5);
        jspAnoPreA44.setViewportView(jtaAnoA44);

        jpaAba1.add(jspAnoPreA44);
        jspAnoPreA44.setBounds(750, 2708, 40, 10);

        txtPreA45.setEditable(false);
        txtPreA45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA45.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA45);
        txtPreA45.setBounds(704, 2729, 109, 28);

        jtbAnoPreA45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA45ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA45);
        jtbAnoPreA45.setBounds(824, 2724, 40, 40);

        btnFotPreA45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA45ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA45);
        btnFotPreA45.setBounds(880, 2724, 40, 40);

        jtaAnoA45.setEditable(false);
        jtaAnoA45.setColumns(20);
        jtaAnoA45.setLineWrap(true);
        jtaAnoA45.setRows(5);
        jspAnoPreA45.setViewportView(jtaAnoA45);

        jpaAba1.add(jspAnoPreA45);
        jspAnoPreA45.setBounds(750, 2761, 40, 10);

        txtPreA46.setEditable(false);
        txtPreA46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA46.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA46);
        txtPreA46.setBounds(704, 2782, 109, 28);

        jtbAnoPreA46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA46ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA46);
        jtbAnoPreA46.setBounds(824, 2776, 40, 40);

        btnFotPreA46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA46ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA46);
        btnFotPreA46.setBounds(880, 2776, 40, 40);

        jtaAnoA46.setEditable(false);
        jtaAnoA46.setColumns(20);
        jtaAnoA46.setLineWrap(true);
        jtaAnoA46.setRows(5);
        jspAnoPreA46.setViewportView(jtaAnoA46);

        jpaAba1.add(jspAnoPreA46);
        jspAnoPreA46.setBounds(750, 2814, 40, 10);

        txtPreA47.setEditable(false);
        txtPreA47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA47.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA47);
        txtPreA47.setBounds(704, 2835, 109, 28);

        jtbAnoPreA47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA47ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA47);
        jtbAnoPreA47.setBounds(824, 2829, 40, 40);

        btnFotPreA47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA47ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA47);
        btnFotPreA47.setBounds(880, 2829, 40, 40);

        jtaAnoA47.setEditable(false);
        jtaAnoA47.setColumns(20);
        jtaAnoA47.setLineWrap(true);
        jtaAnoA47.setRows(5);
        jspAnoPreA47.setViewportView(jtaAnoA47);

        jpaAba1.add(jspAnoPreA47);
        jspAnoPreA47.setBounds(750, 2867, 40, 10);

        txtPreA48.setEditable(false);
        txtPreA48.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPreA48.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jpaAba1.add(txtPreA48);
        txtPreA48.setBounds(704, 2889, 109, 28);

        jtbAnoPreA48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        jtbAnoPreA48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbAnoPreA48ActionPerformed(evt);
            }
        });
        jpaAba1.add(jtbAnoPreA48);
        jtbAnoPreA48.setBounds(824, 2882, 40, 40);

        btnFotPreA48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotPreA48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotPreA48ActionPerformed(evt);
            }
        });
        jpaAba1.add(btnFotPreA48);
        btnFotPreA48.setBounds(880, 2882, 40, 40);

        jtaAnoA48.setEditable(false);
        jtaAnoA48.setColumns(20);
        jtaAnoA48.setLineWrap(true);
        jtaAnoA48.setRows(5);
        jspAnoPreA48.setViewportView(jtaAnoA48);

        jpaAba1.add(jspAnoPreA48);
        jspAnoPreA48.setBounds(750, 2920, 40, 10);

        btnImpPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/8396426_printer_document_business_office_machine_print.png"))); // NOI18N
        btnImpPre.setToolTipText("Imprimir Documento");
        btnImpPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpPreActionPerformed(evt);
            }
        });
        jpaAba1.add(btnImpPre);
        btnImpPre.setBounds(822, 3038, 64, 64);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Elétrica semestral.png"))); // NOI18N
        jpaAba1.add(jLabel18);
        jLabel18.setBounds(-12, 0, 1000, 3158);

        jScrollPane2.setViewportView(jpaAba1);

        jtpAbas.addTab("Eletrica Semestral", jScrollPane2);

        getContentPane().add(jtpAbas);
        jtpAbas.setBounds(-5, 0, 1005, 632);

        setBounds(0, 0, 1005, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened (versão otimizada com reflexão)
     *
     * Acionado ao abrir a janela interna (`JInternalFrame`), este método oculta
     * dinamicamente os componentes `JScrollPane` de observação dos itens A1 a
     * A118.
     *
     * Funcionalidades: - Oculta o painel de arquivos `jpaArqPre`. - Utiliza um
     * laço de 1 a 118 para construir dinamicamente os nomes dos campos
     * `jspAnoPreA{n}`. - Usa reflexão (`getDeclaredField`) para acessar os
     * campos da classe e definir sua visibilidade como `false`. - Em caso de
     * erro (campo inexistente ou inacessível), exibe uma mensagem de alerta via
     * `JOptionPane`.
     *
     * Vantagens: - Elimina repetição de código. - Facilita manutenção e
     * expansão futura. - Garante que todos os campos de observação estejam
     * ocultos ao abrir a janela.
     *
     * @param evt o evento gerado pela abertura da janela interna
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        jpaArqPre.setVisible(false);

        // Laço para ocultar jspAnoPreA1 a jspAnoPreA118
        for (int i = 1; i <= 118; i++) {
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
     * Acionado quando o botão de salvar foto (`btnSalFotPre8`) é clicado.
     *
     * Funcionalidade: - Chama o método `salve_file()`, que permite ao usuário
     * escolher o local e nome para salvar o arquivo da imagem previamente
     * carregada via FTP.
     *
     * Observações: - Esse método é vinculado ao evento de clique do botão na
     * interface gráfica. - A lógica de salvamento e feedback ao usuário está
     * encapsulada no método `salve_file()`.
     *
     * @param evt o evento gerado pelo clique no botão
     */
    private void btnSalFotPre8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalFotPre8ActionPerformed

        salve_file();
    }//GEN-LAST:event_btnSalFotPre8ActionPerformed

    /**
     * Método txtCanFotActionPerformed
     *
     * Acionado quando o botão de cancelar visualização de foto (`txtCanFot`) é
     * clicado.
     *
     * Funcionalidades: - Oculta o painel de arquivos (`jpaArqPre`). - Remove a
     * imagem exibida no `lblFotEltMen8`. - Limpa o campo de nome da foto
     * (`txtFotPre8`). - Desabilita o botão de salvar (`btnSalFotPre8`). - Limpa
     * a tabela de arquivos (`tblArqPre`) zerando suas linhas. - Limpa os campos
     * de pesquisa (`txtPesArq`) e quantidade (`txtQanArq`).
     *
     * Esse método garante que a interface volte ao estado inicial após o
     * cancelamento da visualização, evitando dados residuais ou ações
     * indevidas.
     *
     * @param evt o evento gerado pelo clique no botão de cancelamento
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
     * pesquisa `txtPesArq`.
     *
     * Funcionalidade: - Chama o método `pesq()` para realizar a busca de
     * arquivos conforme o texto digitado. - Permite atualização dinâmica da
     * tabela de arquivos (`tblArqPre`) enquanto o usuário digita.
     *
     * Esse evento melhora a experiência do usuário ao tornar a pesquisa
     * responsiva e em tempo real.
     *
     * @param evt o evento gerado pela liberação de tecla
     */
    private void txtPesArqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesArqKeyReleased

        pesq();
    }//GEN-LAST:event_txtPesArqKeyReleased

    /**
     * Método tblArqPreMouseClicked
     *
     * Acionado automaticamente quando o usuário clica em uma linha da tabela
     * `tblArqPre`.
     *
     * Funcionalidade: - Chama o método `setar_arq()` para capturar os dados do
     * arquivo selecionado. - Inicia os processos de visualização e download do
     * arquivo correspondente.
     *
     * Esse evento permite que o usuário interaja diretamente com os resultados
     * da pesquisa, tornando a navegação e manipulação dos arquivos mais
     * intuitiva.
     *
     * @param evt o evento gerado pelo clique do mouse na tabela
     */
    private void tblArqPreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblArqPreMouseClicked

        setar_arq();
    }//GEN-LAST:event_tblArqPreMouseClicked

    /**
     * Método btnFotPreA48ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A48 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A48", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A48.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A48 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA48ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A48";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA48ActionPerformed

    /**
     * Método btnFotPreA47ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A47 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A47", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A47.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A47 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA47ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A47";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA47ActionPerformed

    /**
     * Método btnFotPreA46ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A46 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A46", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A46.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A46 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA46ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A46";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA46ActionPerformed

    /**
     * Método btnFotPreA45ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A45 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A45", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A45.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A45 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA45ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A45";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA45ActionPerformed

    /**
     * Método jtbAnoPreA45ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA45`) é clicado.
     *
     * Funcionalidades: - Verifica se o botão está selecionado (`isSelected()`).
     * - Se estiver selecionado: - Torna visível o campo de observação
     * `jspAnoPreA45`. - Define seu tamanho como 170x100 pixels. - Se não
     * estiver selecionado: - Oculta o campo `jspAnoPreA45`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A45 conforme sua interação com o botão.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA45ActionPerformed

        if (jtbAnoPreA45.isSelected()) {
            jspAnoPreA45.setVisible(true);
            jspAnoPreA45.setSize(170, 100);
        } else {
            jspAnoPreA45.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA45ActionPerformed

    /**
     * Método btnFotPreA44ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A44 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A44", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A44.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A44 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA44ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A44";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA44ActionPerformed

    /**
     * Método jtbAnoPreA44ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA44`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA44` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA44`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A44.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA44ActionPerformed

        if (jtbAnoPreA44.isSelected()) {
            jspAnoPreA44.setVisible(true);
            jspAnoPreA44.setSize(170, 100);
        } else {
            jspAnoPreA44.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA44ActionPerformed

    /**
     * Método btnFotPreA43ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A43 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A43", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A43.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A43 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA43ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A43";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA43ActionPerformed

    /**
     * Método jtbAnoPreA43ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA43`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA44` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA43`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A43.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA43ActionPerformed

        if (jtbAnoPreA43.isSelected()) {
            jspAnoPreA43.setVisible(true);
            jspAnoPreA43.setSize(170, 100);
        } else {
            jspAnoPreA43.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA43ActionPerformed

    /**
     * Método btnFotPreA42ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A42 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A42", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A42.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A42 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA42ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A42";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA42ActionPerformed

    /**
     * Método jtbAnoPreA42ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA42`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA42` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA42`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A42.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA42ActionPerformed

        if (jtbAnoPreA42.isSelected()) {
            jspAnoPreA42.setVisible(true);
            jspAnoPreA42.setSize(170, 100);
        } else {
            jspAnoPreA42.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA42ActionPerformed

    /**
     * Método btnFotPreA41ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A41 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A41", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A41.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A41 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA41ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A41";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA41ActionPerformed

    /**
     * Método jtbAnoPreA41ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA41`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA41` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA41`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A41.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA41ActionPerformed

        if (jtbAnoPreA41.isSelected()) {
            jspAnoPreA41.setVisible(true);
            jspAnoPreA41.setSize(170, 100);
        } else {
            jspAnoPreA41.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA41ActionPerformed

    /**
     * Método btnFotPreA40ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A40 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A40", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A40.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A40 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA40ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A40";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA40ActionPerformed

    /**
     * Método jtbAnoPreA40ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA40`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA40` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA40`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A40.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA40ActionPerformed

        if (jtbAnoPreA40.isSelected()) {
            jspAnoPreA40.setVisible(true);
            jspAnoPreA40.setSize(170, 100);
        } else {
            jspAnoPreA40.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA40ActionPerformed

    /**
     * Método btnFotPreA39ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A39 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A39", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A39.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A39 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA39ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A39";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA39ActionPerformed

    /**
     * Método jtbAnoPreA39ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA39`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA39` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA39`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A39.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA39ActionPerformed

        if (jtbAnoPreA39.isSelected()) {
            jspAnoPreA39.setVisible(true);
            jspAnoPreA39.setSize(170, 100);
        } else {
            jspAnoPreA39.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA39ActionPerformed

    /**
     * Método btnFotPreA38ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A38 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A38", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A38.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A39 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA38ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A38";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA38ActionPerformed

    /**
     * Método jtbAnoPreA38ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA38`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA38` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA38`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A38.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA38ActionPerformed

        if (jtbAnoPreA38.isSelected()) {
            jspAnoPreA38.setVisible(true);
            jspAnoPreA38.setSize(170, 100);
        } else {
            jspAnoPreA38.setVisible(false);;
        }
    }//GEN-LAST:event_jtbAnoPreA38ActionPerformed

    /**
     * Método btnFotPreA37ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A37 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A37", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A37.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A37 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA37ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A37";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA37ActionPerformed

    /**
     * Método jtbAnoPreA37ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA37`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA37` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA37`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A37.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA37ActionPerformed

        if (jtbAnoPreA37.isSelected()) {
            jspAnoPreA37.setVisible(true);
            jspAnoPreA37.setSize(170, 100);
        } else {
            jspAnoPreA37.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA37ActionPerformed

    /**
     * Método btnFotPreA36ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A36 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A36", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A36.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A37 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA36ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A36";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA36ActionPerformed

    /**
     * Método jtbAnoPreA36ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA36`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA36` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA36`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A36.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA36ActionPerformed

        if (jtbAnoPreA36.isSelected()) {
            jspAnoPreA36.setVisible(true);
            jspAnoPreA36.setSize(170, 100);
        } else {
            jspAnoPreA36.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA36ActionPerformed

    /**
     * Método btnFotPreA35ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A35 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A35", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A35.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A35 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA35ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A35";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA35ActionPerformed

    /**
     * Método jtbAnoPreA35ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA35`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA35` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA35`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A35.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA35ActionPerformed

        if (jtbAnoPreA35.isSelected()) {
            jspAnoPreA35.setVisible(true);
            jspAnoPreA35.setSize(170, 100);
        } else {
            jspAnoPreA35.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA35ActionPerformed

    /**
     * Método btnFotPreA34ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A34 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A34", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A34.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A34 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA34ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A34";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA34ActionPerformed

    /**
     * Método jtbAnoPreA34ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA34`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA34` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA35`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A34.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA34ActionPerformed

        if (jtbAnoPreA34.isSelected()) {
            jspAnoPreA34.setVisible(true);
            jspAnoPreA34.setSize(170, 100);
        } else {
            jspAnoPreA34.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA34ActionPerformed

    /**
     * Método btnFotPreA33ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A33 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A33", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A33.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A33 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA33ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A33";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA33ActionPerformed

    /**
     * Método jtbAnoPreA33ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA33`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA33` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA33`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A33.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA33ActionPerformed

        if (jtbAnoPreA33.isSelected()) {
            jspAnoPreA33.setVisible(true);
            jspAnoPreA33.setSize(170, 100);
        } else {
            jspAnoPreA33.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA33ActionPerformed

    /**
     * Método btnFotPreA32ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A32 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A32", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A32.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A32 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA32ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A32";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA32ActionPerformed

    /**
     * Método jtbAnoPreA33ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA32`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA32` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA32`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A32.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA32ActionPerformed

        if (jtbAnoPreA32.isSelected()) {
            jspAnoPreA32.setVisible(true);
            jspAnoPreA32.setSize(170, 100);
        } else {
            jspAnoPreA32.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA32ActionPerformed

    /**
     * Método btnFotPreA31ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A31 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A31", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A31.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A31 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA31ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A31";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA31ActionPerformed

    /**
     * Método jtbAnoPreA31ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA31`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA31` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA31`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A31.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA31ActionPerformed

        if (jtbAnoPreA31.isSelected()) {
            jspAnoPreA31.setVisible(true);
            jspAnoPreA31.setSize(170, 100);
        } else {
            jspAnoPreA31.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA31ActionPerformed

    /**
     * Método btnFotPreA30ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A30 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A30", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A30.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A31 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA30ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A30";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA30ActionPerformed

    /**
     * Método jtbAnoPreA30ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA30`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA30` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA31`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A30.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA30ActionPerformed

        if (jtbAnoPreA30.isSelected()) {
            jspAnoPreA30.setVisible(true);
            jspAnoPreA30.setSize(170, 100);
        } else {
            jspAnoPreA30.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA30ActionPerformed

    /**
     * Método btnFotPreA29ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A29 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A29", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A29.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A29 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA29ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A29";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA29ActionPerformed

    /**
     * Método jtbAnoPreA29ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA29`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA29` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA31`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A29.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA29ActionPerformed

        if (jtbAnoPreA29.isSelected()) {
            jspAnoPreA29.setVisible(true);
            jspAnoPreA29.setSize(170, 100);
        } else {
            jspAnoPreA29.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA29ActionPerformed

    /**
     * Método btnFotPreA28ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A28 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A28", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A29.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A28 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA28ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA28ActionPerformed

    /**
     * Método jtbAnoPreA28ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA28`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA28` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA31`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A28.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA28ActionPerformed
        // TODO add your handling code here:
        if (jtbAnoPreA28.isSelected()) {
            jspAnoPreA28.setVisible(true);
            jspAnoPreA28.setSize(170, 100);
        } else {
            jspAnoPreA28.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA28ActionPerformed

    /**
     * Método btnFotPreA27ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A27 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A27", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A27.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A27 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA27ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A27";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA27ActionPerformed

    /**
     * Método jtbAnoPreA27ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA27`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA27` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA27`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A27.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA27ActionPerformed

        if (jtbAnoPreA27.isSelected()) {
            jspAnoPreA27.setVisible(true);
            jspAnoPreA27.setSize(170, 100);
        } else {
            jspAnoPreA27.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA27ActionPerformed

    /**
     * Método btnFotPreA26ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A26 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A26", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A26.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A26 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA26ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA26ActionPerformed

    /**
     * Método jtbAnoPreA26ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA26`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA26` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA26`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A26.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA26ActionPerformed

        if (jtbAnoPreA26.isSelected()) {
            jspAnoPreA26.setVisible(true);
            jspAnoPreA26.setSize(170, 100);
        } else {
            jspAnoPreA26.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA26ActionPerformed

    /**
     * Método btnFotPreA25ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A25 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A25", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A25.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A25 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA25ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA25ActionPerformed

    /**
     * Método jtbAnoPreA25ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA25`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA25` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA24`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A25.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA25ActionPerformed

        if (jtbAnoPreA25.isSelected()) {
            jspAnoPreA25.setVisible(true);
            jspAnoPreA25.setSize(170, 100);
        } else {
            jspAnoPreA25.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA25ActionPerformed

    /**
     * Método btnFotPreA24ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A24 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A24", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A24.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A24 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA24ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA24ActionPerformed

    /**
     * Método jtbAnoPreA24ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA24`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA24` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA24`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A24.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA24ActionPerformed

        if (jtbAnoPreA24.isSelected()) {
            jspAnoPreA24.setVisible(true);
            jspAnoPreA24.setSize(170, 100);
        } else {
            jspAnoPreA24.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA24ActionPerformed

    /**
     * Método btnFotPreA23ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A23 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A23", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A23.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A23 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA23ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA23ActionPerformed

    /**
     * Método jtbAnoPreA23ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA23`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA23` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA23`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A23.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA23ActionPerformed

        if (jtbAnoPreA23.isSelected()) {
            jspAnoPreA23.setVisible(true);
            jspAnoPreA23.setSize(170, 100);
        } else {
            jspAnoPreA23.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA23ActionPerformed

    /**
     * Método btnFotPreA22ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A22 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A22", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A22.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A22 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA22ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA22ActionPerformed

    /**
     * Método jtbAnoPreA22ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA22`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA22` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA22`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A22.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA22ActionPerformed

        if (jtbAnoPreA22.isSelected()) {
            jspAnoPreA22.setVisible(true);
            jspAnoPreA22.setSize(170, 100);
        } else {
            jspAnoPreA22.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA22ActionPerformed

    /**
     * Método btnFotPreA21ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A21 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A21", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A21.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A21 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA21ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA21ActionPerformed

    /**
     * Método jtbAnoPreA21ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA21`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA21` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA21`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A21.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA21ActionPerformed

        if (jtbAnoPreA21.isSelected()) {
            jspAnoPreA21.setVisible(true);
            jspAnoPreA21.setSize(170, 100);
        } else {
            jspAnoPreA21.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA21ActionPerformed

    /**
     * Método btnFotPreA20ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A20 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A20", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A20.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A21 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA20ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA20ActionPerformed

    /**
     * Método jtbAnoPreA20ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA20`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA20` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA20`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A20.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA20ActionPerformed

        if (jtbAnoPreA20.isSelected()) {
            jspAnoPreA20.setVisible(true);
            jspAnoPreA20.setSize(170, 100);
        } else {
            jspAnoPreA20.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA20ActionPerformed

    /**
     * Método btnFotPreA19ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A19 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A19", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A19.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A19 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA19ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA19ActionPerformed

    /**
     * Método jtbAnoPreA19ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA19`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA19` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA19`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A19.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA19ActionPerformed

        if (jtbAnoPreA19.isSelected()) {
            jspAnoPreA19.setVisible(true);
            jspAnoPreA19.setSize(170, 100);
        } else {
            jspAnoPreA19.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA19ActionPerformed

    /**
     * Método btnFotPreA18ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A18 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A18", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A18.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A18 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA18ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA18ActionPerformed

    /**
     * Método jtbAnoPreA19ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA19`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA19` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA19`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A19.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA18ActionPerformed

        if (jtbAnoPreA18.isSelected()) {
            jspAnoPreA18.setVisible(true);
            jspAnoPreA18.setSize(170, 100);
        } else {
            jspAnoPreA18.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA18ActionPerformed

    /**
     * Método btnFotPreA17ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A17 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A17", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A17.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A17 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA17ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA17ActionPerformed

    /**
     * Método jtbAnoPreA17ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA17`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA17` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA17`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A17.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA17ActionPerformed

        if (jtbAnoPreA17.isSelected()) {
            jspAnoPreA17.setVisible(true);
            jspAnoPreA17.setSize(170, 100);
        } else {
            jspAnoPreA17.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA17ActionPerformed

    /**
     * Método btnFotPreA16ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A16 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A16", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A16.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A16 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA16ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA16ActionPerformed

    /**
     * Método jtbAnoPreA16ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA16`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA16` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA16`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A16.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA16ActionPerformed

        if (jtbAnoPreA16.isSelected()) {
            jspAnoPreA16.setVisible(true);
            jspAnoPreA16.setSize(170, 100);
        } else {
            jspAnoPreA16.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA16ActionPerformed

    /**
     * Método btnFotPreA15ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A15 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A15", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A15.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A15 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA15ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA15ActionPerformed

    /**
     * Método jtbAnoPreA15ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA15`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA15` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA15`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A15.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA15ActionPerformed

        if (jtbAnoPreA15.isSelected()) {
            jspAnoPreA15.setVisible(true);
            jspAnoPreA15.setSize(170, 100);
        } else {
            jspAnoPreA15.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA15ActionPerformed

    /**
     * Método btnFotPreA14ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A14 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A14", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A14.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A14 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA14ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA14ActionPerformed

    /**
     * Método jtbAnoPreA14ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA14`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA14` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA14`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A14.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA14ActionPerformed

        if (jtbAnoPreA14.isSelected()) {
            jspAnoPreA14.setVisible(true);
            jspAnoPreA14.setSize(170, 100);
        } else {
            jspAnoPreA14.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA14ActionPerformed

    /**
     * Método btnFotPreA13ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A13 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A13", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A13.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A13 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA13ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA13ActionPerformed

    /**
     * Método jtbAnoPreA13ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA13`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA13` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA13`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A13.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA13ActionPerformed

        if (jtbAnoPreA13.isSelected()) {
            jspAnoPreA13.setVisible(true);
            jspAnoPreA13.setSize(170, 100);
        } else {
            jspAnoPreA13.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA13ActionPerformed

    /**
     * Método btnFotPreA12ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A12 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A12", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A12.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A12 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA12ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA12ActionPerformed

    /**
     * Método jtbAnoPreA12ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA12`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA12` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA12`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A12.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA12ActionPerformed

        if (jtbAnoPreA12.isSelected()) {
            jspAnoPreA12.setVisible(true);
            jspAnoPreA12.setSize(170, 100);
        } else {
            jspAnoPreA12.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA12ActionPerformed

    /**
     * Método btnFotPreA11ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A11 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A11", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A11.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A11 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA11ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA11ActionPerformed

    /**
     * Método jtbAnoPreA11ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA11`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA11` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA11`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A11.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA11ActionPerformed

        if (jtbAnoPreA11.isSelected()) {
            jspAnoPreA11.setVisible(true);
            jspAnoPreA11.setSize(170, 100);
        } else {
            jspAnoPreA11.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA11ActionPerformed

    /**
     * Método btnFotPreA10ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A10 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A10", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A10.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A10 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA10ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA10ActionPerformed

    /**
     * Método jtbAnoPreA10ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA10`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA10` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA10`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A10.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA10ActionPerformed

        if (jtbAnoPreA10.isSelected()) {
            jspAnoPreA10.setVisible(true);
            jspAnoPreA10.setSize(170, 100);
        } else {
            jspAnoPreA10.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA10ActionPerformed

    /**
     * Método btnFotPreA9ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A9 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A9", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A9.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A9 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA9ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA9ActionPerformed

    /**
     * Método jtbAnoPreA9ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA9`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA9` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA9`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A9.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA9ActionPerformed

        if (jtbAnoPreA9.isSelected()) {
            jspAnoPreA9.setVisible(true);
            jspAnoPreA9.setSize(170, 100);
        } else {
            jspAnoPreA9.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA9ActionPerformed

    /**
     * Método btnFotPreA8ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A8 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A8", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A8.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A8 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA8ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA8ActionPerformed

    /**
     * Método jtbAnoPreA8ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA8`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA8` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA8`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A8.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA8ActionPerformed

        if (jtbAnoPreA8.isSelected()) {
            jspAnoPreA8.setVisible(true);
            jspAnoPreA8.setSize(170, 100);
        } else {
            jspAnoPreA8.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA8ActionPerformed

    /**
     * Método btnFotPreA7ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A7 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A7", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A7.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A7 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA7ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA7ActionPerformed

    /**
     * Método jtbAnoPreA7ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA7`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA7` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA7`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A7.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA7ActionPerformed

        if (jtbAnoPreA7.isSelected()) {
            jspAnoPreA7.setVisible(true);
            jspAnoPreA7.setSize(170, 100);
        } else {
            jspAnoPreA7.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA7ActionPerformed

    /**
     * Método btnFotPreA6ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A6 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A6", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A6.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A6 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA6ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA6ActionPerformed

    /**
     * Método jtbAnoPreA6ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA6`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA6` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA6`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A6.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA6ActionPerformed

        if (jtbAnoPreA6.isSelected()) {
            jspAnoPreA6.setVisible(true);
            jspAnoPreA6.setSize(170, 100);
        } else {
            jspAnoPreA6.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA6ActionPerformed

    /**
     * Método btnFotPreA5ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A5 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A5", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A5.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A5 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA5ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA5ActionPerformed

    /**
     * Método jtbAnoPreA5ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA5`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA5` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA5`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A5.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA5ActionPerformed

        if (jtbAnoPreA5.isSelected()) {
            jspAnoPreA5.setVisible(true);
            jspAnoPreA5.setSize(170, 100);
        } else {
            jspAnoPreA5.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA5ActionPerformed

    /**
     * Método btnFotPreA4ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A4 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A4", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A4.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A4 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA4ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA4ActionPerformed

    /**
     * Método jtbAnoPreA4ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA4`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA4` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA4`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A4.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA4ActionPerformed

        if (jtbAnoPreA4.isSelected()) {
            jspAnoPreA4.setVisible(true);
            jspAnoPreA4.setSize(170, 100);
        } else {
            jspAnoPreA4.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA4ActionPerformed

    /**
     * Método btnFotPreA3ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A3 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A3", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A3.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A3 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA3ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA3ActionPerformed

    /**
     * Método jtbAnoPreA3ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA3`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA3` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA3`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A3.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA3ActionPerformed

        if (jtbAnoPreA3.isSelected()) {
            jspAnoPreA3.setVisible(true);
            jspAnoPreA3.setSize(170, 100);
        } else {
            jspAnoPreA3.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA3ActionPerformed

    /**
     * Método btnFotPreA2ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A2 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A2", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A2.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A2 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA2ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA2ActionPerformed

    /**
     * Método jtbAnoPreA2ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA2`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA2` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA2`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A2.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA2ActionPerformed

        if (jtbAnoPreA2.isSelected()) {
            jspAnoPreA2.setVisible(true);
            jspAnoPreA2.setSize(170, 100);
        } else {
            jspAnoPreA2.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA2ActionPerformed

    /**
     * Método btnFotPreA1ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A1 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A1", indicando o item selecionado. - Torna visível o painel
     * de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. - Chama o
     * método `arq_pre()` para carregar os arquivos relacionados ao item A1.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A1 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA1ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre();
    }//GEN-LAST:event_btnFotPreA1ActionPerformed

    /**
     * Método jtbAnoPreA1ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA1`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA1` com tamanho 170x100. - Se não estiver selecionado: - Oculta
     * o campo `jspAnoPreA1`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A1.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA1ActionPerformed

        if (jtbAnoPreA1.isSelected()) {
            jspAnoPreA1.setVisible(true);
            jspAnoPreA1.setSize(170, 100);
        } else {
            jspAnoPreA1.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA1ActionPerformed

    /**
     * Método btnFotPreA86ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A86 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A86", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A38.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A86 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA86ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A38";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA86ActionPerformed

    /**
     * Método jtbAnoPreA86ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA86`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA86` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA86`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A86.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA86ActionPerformed

        if (jtbAnoPreA86.isSelected()) {
            jspAnoPreA86.setVisible(true);
            jspAnoPreA86.setSize(170, 100);
        } else {
            jspAnoPreA86.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA86ActionPerformed

    /**
     * Método btnFotPreA85ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A85 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A85", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A37.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A85 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA85ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A37";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA85ActionPerformed

    /**
     * Método jtbAnoPreA85ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA85`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA85` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA85`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A85.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA85ActionPerformed

        if (jtbAnoPreA85.isSelected()) {
            jspAnoPreA85.setVisible(true);
            jspAnoPreA85.setSize(170, 100);
        } else {
            jspAnoPreA85.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA85ActionPerformed

    /**
     * Método btnFotPreA84ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A84 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A84", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A36.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A84 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA84ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A36";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA84ActionPerformed

    /**
     * Método jtbAnoPreA84ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA84`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA84` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA84`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A84.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA84ActionPerformed

        if (jtbAnoPreA84.isSelected()) {
            jspAnoPreA84.setVisible(true);
            jspAnoPreA84.setSize(170, 100);
        } else {
            jspAnoPreA84.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA84ActionPerformed

    /**
     * Método btnFotPreA83ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A83 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A84", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A35.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A83 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA83ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A35";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA83ActionPerformed

    /**
     * Método jtbAnoPreA83ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA83`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA83` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA83`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A83.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA83ActionPerformed

        if (jtbAnoPreA83.isSelected()) {
            jspAnoPreA83.setVisible(true);
            jspAnoPreA83.setSize(170, 100);
        } else {
            jspAnoPreA83.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA83ActionPerformed

    /**
     * Método btnFotPreA82ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A82 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A82", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A34.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A82 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA82ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A34";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA82ActionPerformed

    /**
     * Método jtbAnoPreA82ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA82`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA82` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA82`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A82.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA82ActionPerformed

        if (jtbAnoPreA82.isSelected()) {
            jspAnoPreA82.setVisible(true);
            jspAnoPreA82.setSize(170, 100);
        } else {
            jspAnoPreA82.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA82ActionPerformed

    /**
     * Método btnFotPreA81ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A81 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A81", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A33.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A81 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA81ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A33";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA81ActionPerformed

    /**
     * Método jtbAnoPreA81ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA81`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA81` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA81`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A81.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA81ActionPerformed

        if (jtbAnoPreA81.isSelected()) {
            jspAnoPreA81.setVisible(true);
            jspAnoPreA81.setSize(170, 100);
        } else {
            jspAnoPreA81.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA81ActionPerformed

    /**
     * Método btnFotPreA80ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A80 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A80", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A32.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A80 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA80ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A32";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA80ActionPerformed

    /**
     * Método jtbAnoPreA80ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA80`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA80` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA80`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A80.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA80ActionPerformed

        if (jtbAnoPreA80.isSelected()) {
            jspAnoPreA80.setVisible(true);
            jspAnoPreA80.setSize(170, 100);
        } else {
            jspAnoPreA80.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA80ActionPerformed

    /**
     * Método btnFotPreA79ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A79 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A79", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A31.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A79 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA79ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A31";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA79ActionPerformed

    /**
     * Método jtbAnoPreA79ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA79`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA79` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA79`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A79.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA79ActionPerformed

        if (jtbAnoPreA79.isSelected()) {
            jspAnoPreA79.setVisible(true);
            jspAnoPreA79.setSize(170, 100);
        } else {
            jspAnoPreA79.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA79ActionPerformed

    /**
     * Método btnFotPreA78ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A78 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A78", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A30.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A78 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA78ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A30";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA78ActionPerformed

    /**
     * Método jtbAnoPreA78ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA78`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA78` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA78`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A78.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA78ActionPerformed

        if (jtbAnoPreA78.isSelected()) {
            jspAnoPreA78.setVisible(true);
            jspAnoPreA78.setSize(170, 100);
        } else {
            jspAnoPreA78.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA78ActionPerformed

    /**
     * Método btnFotPreA77ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A77 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A77", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A29.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A77 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA77ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A29";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA77ActionPerformed

    /**
     * Método jtbAnoPreA77ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA77`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA77` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA77`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A77.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA77ActionPerformed

        if (jtbAnoPreA77.isSelected()) {
            jspAnoPreA77.setVisible(true);
            jspAnoPreA77.setSize(170, 100);
        } else {
            jspAnoPreA77.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA77ActionPerformed

    /**
     * Método btnFotPreA76ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A76 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A76", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A28.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A76 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA76ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA76ActionPerformed

    /**
     * Método jtbAnoPreA76ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA76`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA76` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA76`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A76.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA76ActionPerformed

        if (jtbAnoPreA76.isSelected()) {
            jspAnoPreA76.setVisible(true);
            jspAnoPreA76.setSize(170, 100);
        } else {
            jspAnoPreA76.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA76ActionPerformed

    /**
     * Método btnFotPreA75ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A75 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A75", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A27.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A75 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA75ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A27";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA75ActionPerformed

    /**
     * Método jtbAnoPreA75ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA75`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA75` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA75`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A75.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA75ActionPerformed

        if (jtbAnoPreA75.isSelected()) {
            jspAnoPreA75.setVisible(true);
            jspAnoPreA75.setSize(170, 100);
        } else {
            jspAnoPreA75.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA75ActionPerformed

    /**
     * Método btnFotPreA74ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A74 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A74", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A26.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A74 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA74ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA74ActionPerformed

    /**
     * Método jtbAnoPreA74ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA74`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA74` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA74`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A74.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA74ActionPerformed

        if (jtbAnoPreA74.isSelected()) {
            jspAnoPreA74.setVisible(true);
            jspAnoPreA74.setSize(170, 100);
        } else {
            jspAnoPreA74.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA74ActionPerformed

    /**
     * Método btnFotPreA73ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A73 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A73", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A25.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A73 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA73ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA73ActionPerformed

    /**
     * Método jtbAnoPreA73ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA73`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA73` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA73`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A73.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA73ActionPerformed

        if (jtbAnoPreA73.isSelected()) {
            jspAnoPreA73.setVisible(true);
            jspAnoPreA73.setSize(170, 100);
        } else {
            jspAnoPreA73.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA73ActionPerformed

    /**
     * Método btnFotPreA72ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A72 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A72", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A24.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A72 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA72ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA72ActionPerformed

    /**
     * Método jtbAnoPreA72ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA72`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA72` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA72`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A72.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA72ActionPerformed

        if (jtbAnoPreA72.isSelected()) {
            jspAnoPreA72.setVisible(true);
            jspAnoPreA72.setSize(170, 100);
        } else {
            jspAnoPreA72.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA72ActionPerformed

    /**
     * Método btnFotPreA71ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A71 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A71", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A23.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A71 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA71ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA71ActionPerformed

    /**
     * Método jtbAnoPreA71ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA71`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA71` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA71`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item A71.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA71ActionPerformed

        if (jtbAnoPreA71.isSelected()) {
            jspAnoPreA71.setVisible(true);
            jspAnoPreA71.setSize(170, 100);
        } else {
            jspAnoPreA71.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA71ActionPerformed

    /**
     * Método btnFotPreA70ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A70 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A70", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A22.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A70 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA70ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA70ActionPerformed

    /**
     * Método jtbAnoPreA70ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA70`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA70` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA70`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 70.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA70ActionPerformed

        if (jtbAnoPreA70.isSelected()) {
            jspAnoPreA70.setVisible(true);
            jspAnoPreA70.setSize(170, 100);
        } else {
            jspAnoPreA70.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA70ActionPerformed

    /**
     * Método btnFotPreA69ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A69 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A69", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A21.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A69 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA69ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA69ActionPerformed

    /**
     * Método jtbAnoPreA69ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA69`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA69` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA69`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 69.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA69ActionPerformed

        if (jtbAnoPreA69.isSelected()) {
            jspAnoPreA69.setVisible(true);
            jspAnoPreA69.setSize(170, 100);
        } else {
            jspAnoPreA69.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA69ActionPerformed

    /**
     * Método btnFotPreA68ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A68 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A68", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A20.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A68 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA68ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA68ActionPerformed

    /**
     * Método jtbAnoPreA68ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA68`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA68` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA68`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 68.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA68ActionPerformed

        if (jtbAnoPreA68.isSelected()) {
            jspAnoPreA68.setVisible(true);
            jspAnoPreA68.setSize(170, 100);
        } else {
            jspAnoPreA68.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA68ActionPerformed

    /**
     * Método btnFotPreA67ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A67 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A67", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A19.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A67 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA67ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA67ActionPerformed

    /**
     * Método jtbAnoPreA67ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA67`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA67` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA68`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 67.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA67ActionPerformed

        if (jtbAnoPreA67.isSelected()) {
            jspAnoPreA67.setVisible(true);
            jspAnoPreA67.setSize(170, 100);
        } else {
            jspAnoPreA67.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA67ActionPerformed

    /**
     * Método btnFotPreA66ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A66 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A66", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A18.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A66 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA66ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA66ActionPerformed

    /**
     * Método jtbAnoPreA66ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA66`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA66` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA66`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 66.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA66ActionPerformed

        if (jtbAnoPreA66.isSelected()) {
            jspAnoPreA66.setVisible(true);
            jspAnoPreA66.setSize(170, 100);
        } else {
            jspAnoPreA66.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA66ActionPerformed

    /**
     * Método btnFotPreA65ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A65 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A65", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A17.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A65 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA65ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA65ActionPerformed

    /**
     * Método jtbAnoPreA65ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA65`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA65` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA65`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 65.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA65ActionPerformed

        if (jtbAnoPreA65.isSelected()) {
            jspAnoPreA65.setVisible(true);
            jspAnoPreA65.setSize(170, 100);
        } else {
            jspAnoPreA65.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA65ActionPerformed

    /**
     * Método btnFotPreA64ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A64 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A64", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A16.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A64 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA64ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA64ActionPerformed

    /**
     * Método jtbAnoPreA64ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA64`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA64` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA64`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 64.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA64ActionPerformed

        if (jtbAnoPreA64.isSelected()) {
            jspAnoPreA64.setVisible(true);
            jspAnoPreA64.setSize(170, 100);
        } else {
            jspAnoPreA64.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA64ActionPerformed

    /**
     * Método btnFotPreA63ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A63 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A63", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A15.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A63 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA63ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA63ActionPerformed

    /**
     * Método jtbAnoPreA63ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA63`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA63` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA63`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 63.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA63ActionPerformed

        if (jtbAnoPreA63.isSelected()) {
            jspAnoPreA63.setVisible(true);
            jspAnoPreA63.setSize(170, 100);
        } else {
            jspAnoPreA63.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA63ActionPerformed

    /**
     * Método btnFotPreA62ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A62 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A62", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A14.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A62 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA62ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA62ActionPerformed

    /**
     * Método jtbAnoPreA62ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA62`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA62` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA63`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 62.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA62ActionPerformed

        if (jtbAnoPreA62.isSelected()) {
            jspAnoPreA62.setVisible(true);
            jspAnoPreA62.setSize(170, 100);
        } else {
            jspAnoPreA62.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA62ActionPerformed

    /**
     * Método btnFotPreA61ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A61 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A61", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A13.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A61 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA61ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA61ActionPerformed

    /**
     * Método jtbAnoPreA61ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA61`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA61` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA63`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 61.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA61ActionPerformed

        if (jtbAnoPreA61.isSelected()) {
            jspAnoPreA61.setVisible(true);
            jspAnoPreA61.setSize(170, 100);
        } else {
            jspAnoPreA61.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA61ActionPerformed

    /**
     * Método btnFotPreA60ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A60 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A60", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A12.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A60 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA60ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA60ActionPerformed

    /**
     * Método jtbAnoPreA60ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA60`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA61` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA63`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 60.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA60ActionPerformed

        if (jtbAnoPreA60.isSelected()) {
            jspAnoPreA60.setVisible(true);
            jspAnoPreA60.setSize(170, 100);
        } else {
            jspAnoPreA60.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA60ActionPerformed

    /**
     * Método btnFotPreA59ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A59 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A59", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A59.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A11 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA59ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA59ActionPerformed

    /**
     * Método jtbAnoPreA59ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA59`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA59` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA63`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 59.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA59ActionPerformed

        if (jtbAnoPreA59.isSelected()) {
            jspAnoPreA59.setVisible(true);
            jspAnoPreA59.setSize(170, 100);
        } else {
            jspAnoPreA59.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA59ActionPerformed

    /**
     * Método btnFotPreA58ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A58 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A58", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A10.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A58 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA58ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA58ActionPerformed

    /**
     * Método jtbAnoPreA58ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA58`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA58` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA58`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 58.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA58ActionPerformed

        if (jtbAnoPreA58.isSelected()) {
            jspAnoPreA58.setVisible(true);
            jspAnoPreA58.setSize(170, 100);
        } else {
            jspAnoPreA58.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA58ActionPerformed

    /**
     * Método btnFotPreA57ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A57 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A57", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A9.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A57 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA57ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA57ActionPerformed

    /**
     * Método jtbAnoPreA58ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA58`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA58` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA58`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 58.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA57ActionPerformed

        if (jtbAnoPreA57.isSelected()) {
            jspAnoPreA57.setVisible(true);
            jspAnoPreA57.setSize(170, 100);
        } else {
            jspAnoPreA57.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA57ActionPerformed

    /**
     * Método btnFotPreA56ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A56 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A56", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A8.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A58 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA56ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA56ActionPerformed

    /**
     * Método jtbAnoPreA56ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA56`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA56` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA56`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 56.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA56ActionPerformed

        if (jtbAnoPreA56.isSelected()) {
            jspAnoPreA56.setVisible(true);
            jspAnoPreA56.setSize(170, 100);
        } else {
            jspAnoPreA56.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA56ActionPerformed

    /**
     * Método btnFotPreA55ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A55 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A55", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A7.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A58 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA55ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA55ActionPerformed

    /**
     * Método jtbAnoPreA55ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA55`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA55` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA55`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 55.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA55ActionPerformed

        if (jtbAnoPreA55.isSelected()) {
            jspAnoPreA55.setVisible(true);
            jspAnoPreA55.setSize(170, 100);
        } else {
            jspAnoPreA55.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA55ActionPerformed

    /**
     * Método btnFotPreA54ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A54 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A54", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A6.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A54 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA54ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA54ActionPerformed

    /**
     * Método jtbAnoPreA54ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA54`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA54` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA54`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 54.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA54ActionPerformed

        if (jtbAnoPreA54.isSelected()) {
            jspAnoPreA54.setVisible(true);
            jspAnoPreA54.setSize(170, 100);
        } else {
            jspAnoPreA54.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA54ActionPerformed

    /**
     * Método btnFotPreA53ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A53 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A54", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A5.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A53 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA53ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA53ActionPerformed

    /**
     * Método jtbAnoPreA53ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA53`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA53` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA53`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 53.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA53ActionPerformed

        if (jtbAnoPreA53.isSelected()) {
            jspAnoPreA53.setVisible(true);
            jspAnoPreA53.setSize(170, 100);
        } else {
            jspAnoPreA53.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA53ActionPerformed

    /**
     * Método btnFotPreA52ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A52 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A52", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A4.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A52 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA52ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA52ActionPerformed

    /**
     * Método jtbAnoPreA52ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA52`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA52` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA52`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 52.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA52ActionPerformed

        if (jtbAnoPreA52.isSelected()) {
            jspAnoPreA52.setVisible(true);
            jspAnoPreA52.setSize(170, 100);
        } else {
            jspAnoPreA52.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA52ActionPerformed

    /**
     * Método btnFotPreA51ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A51 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A51", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A3.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A51 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA51ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA51ActionPerformed

    /**
     * Método jtbAnoPreA51ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA51`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA51` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA51`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 51.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA51ActionPerformed

        if (jtbAnoPreA51.isSelected()) {
            jspAnoPreA51.setVisible(true);
            jspAnoPreA51.setSize(170, 100);
        } else {
            jspAnoPreA51.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA51ActionPerformed

    /**
     * Método btnFotPreA50ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A50 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A50", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A2.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A51 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA50ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA50ActionPerformed

    /**
     * Método jtbAnoPreA50ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA50`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA50` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA51`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 50.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA50ActionPerformed

        if (jtbAnoPreA50.isSelected()) {
            jspAnoPreA50.setVisible(true);
            jspAnoPreA50.setSize(170, 100);
        } else {
            jspAnoPreA50.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA50ActionPerformed

    /**
     * Método btnFotPreA49ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A49 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A49", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A1.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A49 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA49ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA49ActionPerformed

    /**
     * Método jtbAnoPreA49ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA49`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA49` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA51`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 49.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA49ActionPerformed

        if (jtbAnoPreA49.isSelected()) {
            jspAnoPreA49.setVisible(true);
            jspAnoPreA49.setSize(170, 100);
        } else {
            jspAnoPreA49.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA49ActionPerformed

    /**
     * Método jtbAnoPreA87ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA87`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA88` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA87`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 87.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA87ActionPerformed

        if (jtbAnoPreA87.isSelected()) {
            jspAnoPreA87.setVisible(true);
            jspAnoPreA87.setSize(170, 100);
        } else {
            jspAnoPreA87.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA87ActionPerformed

    /**
     * Método btnFotPreA87ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A87 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A87", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A87.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A87 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA87ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A1";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA87ActionPerformed

    /**
     * Método jtbAnoPreA88ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA88`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA88` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA88`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 88.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA88ActionPerformed

        if (jtbAnoPreA88.isSelected()) {
            jspAnoPreA88.setVisible(true);
            jspAnoPreA88.setSize(170, 100);
        } else {
            jspAnoPreA88.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA88ActionPerformed

    /**
     * Método btnFotPreA88ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A87 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A88", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A88.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A88 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA88ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A2";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA88ActionPerformed

    /**
     * Método jtbAnoPreA89ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA89`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA89` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA89`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 89.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA89ActionPerformed

        if (jtbAnoPreA89.isSelected()) {
            jspAnoPreA89.setVisible(true);
            jspAnoPreA89.setSize(170, 100);
        } else {
            jspAnoPreA89.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA89ActionPerformed

    /**
     * Método btnFotPreA89ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A89 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A89", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A3.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A89 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA89ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A3";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA89ActionPerformed

    /**
     * Método jtbAnoPreA90ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA90`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA90` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA90`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 90.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA90ActionPerformed

        if (jtbAnoPreA90.isSelected()) {
            jspAnoPreA90.setVisible(true);
            jspAnoPreA90.setSize(170, 100);
        } else {
            jspAnoPreA90.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA90ActionPerformed

    /**
     * Método btnFotPreA90ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A90 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A90", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A4.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A90 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA90ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A4";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA90ActionPerformed

    /**
     * Método jtbAnoPreA91ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA91`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA91` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA90`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 91.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA91ActionPerformed

        if (jtbAnoPreA91.isSelected()) {
            jspAnoPreA91.setVisible(true);
            jspAnoPreA91.setSize(170, 100);
        } else {
            jspAnoPreA91.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA91ActionPerformed

    /**
     * Método btnFotPreA91ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A91 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A91", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A5.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A91 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA91ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A5";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre1();
    }//GEN-LAST:event_btnFotPreA91ActionPerformed

    /**
     * Método jtbAnoPreA92ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA92`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA92` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA92`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 92.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA92ActionPerformed

        if (jtbAnoPreA92.isSelected()) {
            jspAnoPreA92.setVisible(true);
            jspAnoPreA92.setSize(170, 100);
        } else {
            jspAnoPreA92.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA92ActionPerformed

    /**
     * Método btnFotPreA92ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A92 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A92", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A6.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A92 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA92ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A6";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA92ActionPerformed

    /**
     * Método jtbAnoPreA93ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA93`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA93` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA93`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 93.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA93ActionPerformed

        if (jtbAnoPreA93.isSelected()) {
            jspAnoPreA93.setVisible(true);
            jspAnoPreA93.setSize(170, 100);
        } else {
            jspAnoPreA93.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA93ActionPerformed

    /**
     * Método btnFotPreA93ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A93 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A93", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A7.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A93 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA93ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A7";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA93ActionPerformed

    /**
     * Método jtbAnoPreA94ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA94`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA94` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA94`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 94.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA94ActionPerformed

        if (jtbAnoPreA94.isSelected()) {
            jspAnoPreA94.setVisible(true);
            jspAnoPreA94.setSize(170, 100);
        } else {
            jspAnoPreA94.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA94ActionPerformed

    /**
     * Método btnFotPreA94ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A94 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A94", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A8.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A94 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA94ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A8";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA94ActionPerformed

    /**
     * Método jtbAnoPreA95ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA95`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA95` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA95`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 95.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA95ActionPerformed

        if (jtbAnoPreA95.isSelected()) {
            jspAnoPreA95.setVisible(true);
            jspAnoPreA95.setSize(170, 100);
        } else {
            jspAnoPreA95.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA95ActionPerformed

    /**
     * Método btnFotPreA95ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A95 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A95", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A9.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A95 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA95ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A9";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA95ActionPerformed

    /**
     * Método jtbAnoPreA96ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA96`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA96` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA96`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 96.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA96ActionPerformed

        if (jtbAnoPreA96.isSelected()) {
            jspAnoPreA96.setVisible(true);
            jspAnoPreA96.setSize(170, 100);
        } else {
            jspAnoPreA96.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA96ActionPerformed

    /**
     * Método btnFotPreA96ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A96 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A96", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A10.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A96 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA96ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A10";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA96ActionPerformed

    /**
     * Método jtbAnoPreA97ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA97`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA97` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA97`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 97.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA97ActionPerformed

        if (jtbAnoPreA97.isSelected()) {
            jspAnoPreA97.setVisible(true);
            jspAnoPreA97.setSize(170, 100);
        } else {
            jspAnoPreA97.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA97ActionPerformed

    /**
     * Método btnFotPreA97ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A97 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A97", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A11.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A97 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA97ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A11";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA97ActionPerformed

    /**
     * Método jtbAnoPreA98ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA98`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA98` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA97`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 98.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA98ActionPerformed

        if (jtbAnoPreA98.isSelected()) {
            jspAnoPreA98.setVisible(true);
            jspAnoPreA98.setSize(170, 100);
        } else {
            jspAnoPreA98.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA98ActionPerformed

    /**
     * Método btnFotPreA98ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A98 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A98", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A12.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A98 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA98ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A12";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA98ActionPerformed

    /**
     * Método jtbAnoPreA99ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA99`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA99` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA97`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 99.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA99ActionPerformed

        if (jtbAnoPreA99.isSelected()) {
            jspAnoPreA99.setVisible(true);
            jspAnoPreA99.setSize(170, 100);
        } else {
            jspAnoPreA99.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA99ActionPerformed

    /**
     * Método btnFotPreA99ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A99 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A99", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A13.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A99 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA99ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A13";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA99ActionPerformed

    /**
     * Método jtbAnoPreA100ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA100`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA100` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA100`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 100.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA100ActionPerformed

        if (jtbAnoPreA100.isSelected()) {
            jspAnoPreA100.setVisible(true);
            jspAnoPreA100.setSize(170, 100);
        } else {
            jspAnoPreA100.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA100ActionPerformed

    /**
     * Método btnFotPreA100ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A100 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A100", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A14.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A100 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA100ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A14";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA100ActionPerformed

    /**
     * Método jtbAnoPreA101ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA101`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA101` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA101`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 101.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA101ActionPerformed

        if (jtbAnoPreA101.isSelected()) {
            jspAnoPreA101.setVisible(true);
            jspAnoPreA101.setSize(170, 100);
        } else {
            jspAnoPreA101.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA101ActionPerformed

    /**
     * Método btnFotPreA101ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A101 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A101", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A15.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A101 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA101ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A15";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA101ActionPerformed

    /**
     * Método jtbAnoPreA102ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA102`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA102` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA102`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 102.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA102ActionPerformed

        if (jtbAnoPreA102.isSelected()) {
            jspAnoPreA102.setVisible(true);
            jspAnoPreA102.setSize(170, 100);
        } else {
            jspAnoPreA102.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA102ActionPerformed

    /**
     * Método btnFotPreA102ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A102 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A102", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A16.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A102 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA102ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A16";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA102ActionPerformed

    /**
     * Método jtbAnoPreA103ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA103`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA103` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA103`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 103.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA103ActionPerformed

        if (jtbAnoPreA103.isSelected()) {
            jspAnoPreA103.setVisible(true);
            jspAnoPreA103.setSize(170, 100);
        } else {
            jspAnoPreA103.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA103ActionPerformed

    /**
     * Método btnFotPreA103ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A103 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A103", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A17.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A103 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA103ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A17";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA103ActionPerformed

    /**
     * Método jtbAnoPreA104ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA104`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA104` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA104`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 104.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA104ActionPerformed

        if (jtbAnoPreA104.isSelected()) {
            jspAnoPreA104.setVisible(true);
            jspAnoPreA104.setSize(170, 100);
        } else {
            jspAnoPreA104.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA104ActionPerformed

    /**
     * Método btnFotPreA104ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A104 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A104", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A18.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A104 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA104ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A18";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA104ActionPerformed

    /**
     * Método jtbAnoPreA105ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA105`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA105` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA105`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 105.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA105ActionPerformed

        if (jtbAnoPreA105.isSelected()) {
            jspAnoPreA105.setVisible(true);
            jspAnoPreA105.setSize(170, 100);
        } else {
            jspAnoPreA105.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA105ActionPerformed

    /**
     * Método btnFotPreA105ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A105 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A105", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A19.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A105 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA105ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A19";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA105ActionPerformed

    /**
     * Método jtbAnoPreA106ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA106`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA106` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA106`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 106.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA106ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA106ActionPerformed

        if (jtbAnoPreA106.isSelected()) {
            jspAnoPreA106.setVisible(true);
            jspAnoPreA106.setSize(170, 100);
        } else {
            jspAnoPreA106.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA106ActionPerformed

    /**
     * Método btnFotPreA106ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A106 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A106", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A20.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A106 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA106ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA106ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A20";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA106ActionPerformed

    /**
     * Método jtbAnoPreA107ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA107`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA107` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA107`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 107.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA107ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA107ActionPerformed

        if (jtbAnoPreA107.isSelected()) {
            jspAnoPreA107.setVisible(true);
            jspAnoPreA107.setSize(170, 100);
        } else {
            jspAnoPreA107.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA107ActionPerformed

    /**
     * Método btnFotPreA107ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A107 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A107", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A21.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A107 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA107ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA107ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A21";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA107ActionPerformed

    /**
     * Método jtbAnoPreA108ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA108`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA108` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA108`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 108.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA108ActionPerformed

        if (jtbAnoPreA108.isSelected()) {
            jspAnoPreA108.setVisible(true);
            jspAnoPreA108.setSize(170, 100);
        } else {
            jspAnoPreA108.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA108ActionPerformed

    /**
     * Método btnFotPreA108ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A108 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A108", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A22.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A108 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA108ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A22";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA108ActionPerformed

    /**
     * Método jtbAnoPreA109ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA109`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA109` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA109`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 109.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA109ActionPerformed

        if (jtbAnoPreA109.isSelected()) {
            jspAnoPreA109.setVisible(true);
            jspAnoPreA109.setSize(170, 100);
        } else {
            jspAnoPreA109.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA109ActionPerformed

    /**
     * Método btnFotPreA109ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A109 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A108", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A23.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A109 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA109ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A23";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA109ActionPerformed

    /**
     * Método jtbAnoPreA110ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA110`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA110` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA110`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 110.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA110ActionPerformed

        if (jtbAnoPreA110.isSelected()) {
            jspAnoPreA110.setVisible(true);
            jspAnoPreA110.setSize(170, 100);
        } else {
            jspAnoPreA110.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA110ActionPerformed

    /**
     * Método btnFotPreA110ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A110 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A110", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A24.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A110 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA110ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A24";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA110ActionPerformed

    /**
     * Método jtbAnoPreA111ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA111`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA111` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA111`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 111.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA111ActionPerformed

        if (jtbAnoPreA111.isSelected()) {
            jspAnoPreA111.setVisible(true);
            jspAnoPreA111.setSize(170, 100);
        } else {
            jspAnoPreA111.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA111ActionPerformed

    /**
     * Método btnFotPreA111ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A111 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A111", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A25.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A111 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA111ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A25";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA111ActionPerformed

    /**
     * Método jtbAnoPreA112ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA112`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA112` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA112`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 112.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA112ActionPerformed

        if (jtbAnoPreA112.isSelected()) {
            jspAnoPreA112.setVisible(true);
            jspAnoPreA112.setSize(170, 100);
        } else {
            jspAnoPreA112.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA112ActionPerformed

    /**
     * Método btnFotPreA112ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A112 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A112", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A26.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A112 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA112ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A26";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA112ActionPerformed

    /**
     * Método jtbAnoPreA113ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA113`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA113` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA113`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 113.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA113ActionPerformed

        if (jtbAnoPreA113.isSelected()) {
            jspAnoPreA113.setVisible(true);
            jspAnoPreA113.setSize(170, 100);
        } else {
            jspAnoPreA113.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA113ActionPerformed

    /**
     * Método btnFotPreA113ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A113 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A113", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A27.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A113 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA113ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A27";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA113ActionPerformed

    /**
     * Método jtbAnoPreA114ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA114`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA114` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA114`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 114.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA114ActionPerformed

        if (jtbAnoPreA114.isSelected()) {
            jspAnoPreA114.setVisible(true);
            jspAnoPreA114.setSize(170, 100);
        } else {
            jspAnoPreA114.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA114ActionPerformed

    /**
     * Método btnFotPreA114ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A114 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A114", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A28.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A114 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA114ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A28";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA114ActionPerformed

    /**
     * Método jtbAnoPreA115ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA115`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA115` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA115`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 115.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA115ActionPerformed

        if (jtbAnoPreA115.isSelected()) {
            jspAnoPreA115.setVisible(true);
            jspAnoPreA115.setSize(170, 100);
        } else {
            jspAnoPreA115.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA115ActionPerformed

    /**
     * Método btnFotPreA115ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A115 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A115", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A29.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A115 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA115ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A29";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA115ActionPerformed

    /**
     * Método jtbAnoPreA116ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA116`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA116` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA116`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 116.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA116ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA116ActionPerformed

        if (jtbAnoPreA116.isSelected()) {
            jspAnoPreA116.setVisible(true);
            jspAnoPreA116.setSize(170, 100);
        } else {
            jspAnoPreA116.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA116ActionPerformed

    /**
     * Método btnFotPreA116ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A116 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A116", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A30.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A116 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA116ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA116ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A30";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA116ActionPerformed

    /**
     * Método jtbAnoPreA117ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA117`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA117` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA117`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 117.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA117ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA117ActionPerformed

        if (jtbAnoPreA117.isSelected()) {
            jspAnoPreA117.setVisible(true);
            jspAnoPreA117.setSize(170, 100);
        } else {
            jspAnoPreA117.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA117ActionPerformed

    /**
     * Método btnFotPreA117ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A117 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A117", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A31.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A117 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA117ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA117ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A31";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA117ActionPerformed

    /**
     * Método jtbAnoPreA118ActionPerformed
     *
     * Acionado quando o botão de alternância (`jtbAnoPreA118`) é clicado.
     *
     * Funcionalidades: - Se estiver selecionado: - Exibe o campo de observação
     * `jspAnoPreA118` com tamanho 170x100. - Se não estiver selecionado: -
     * Oculta o campo `jspAnoPreA118`.
     *
     * Esse método permite ao usuário expandir ou recolher o campo de observação
     * do item 118.
     *
     * @param evt o evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA118ActionPerformed

        if (jtbAnoPreA118.isSelected()) {
            jspAnoPreA118.setVisible(true);
            jspAnoPreA118.setSize(170, 100);
        } else {
            jspAnoPreA118.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA118ActionPerformed

    /**
     * Método btnFotPreA118ActionPerformed
     *
     * Acionado quando o botão de visualização de foto do item A118 é clicado.
     *
     * Funcionalidades: - Limpa a imagem exibida (`lblFotEltMen8`) e os campos
     * de texto (`txtFotPre8`, `txtPesArq`). - Desabilita o botão de salvar
     * (`btnSalFotPre8`) para evitar ações indevidas. - Define o nome do arquivo
     * (`arq`) como "A118", indicando o item selecionado. - Torna visível o
     * painel de arquivos (`jpaArqPre`) e ajusta seu tamanho para exibição. -
     * Chama o método `arq_pre()` para carregar os arquivos relacionados ao item
     * A32.
     *
     * Esse método garante que a interface esteja pronta para exibir os arquivos
     * do item A118 de forma limpa e organizada.
     *
     * @param evt o evento gerado pelo clique no botão de visualização
     */
    private void btnFotPreA118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotPreA118ActionPerformed

        lblFotEltMen8.setIcon(null);
        txtFotPre8.setText(null);
        txtPesArq.setText(null);
        btnSalFotPre8.setEnabled(false);

        String nome1 = "A32";
        arq = nome1;
        jpaArqPre.setVisible(true);
        jpaArqPre.setSize(270, 363);
        arq_pre2();
    }//GEN-LAST:event_btnFotPreA118ActionPerformed

    /**
     * Método btnImpPreActionPerformed
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
    private void btnImpPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPreActionPerformed

        prev = "semestral";
        imprimir();
    }//GEN-LAST:event_btnImpPreActionPerformed

    /**
     * Método btnImpPre1ActionPerformed
     *
     * Acionado quando o botão de impressão da preventiva elétrica trimestral é
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
    private void btnImpPre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPre1ActionPerformed

        prev = "trimestral";
        imprimir();
    }//GEN-LAST:event_btnImpPre1ActionPerformed

    /**
     * Método btnImpPre2ActionPerformed
     *
     * Acionado quando o botão de impressão da preventiva elétrica mensal é
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
    private void btnImpPre2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpPre2ActionPerformed

        prev = "mensal";
        imprimir();
    }//GEN-LAST:event_btnImpPre2ActionPerformed

    /**
     * Método jtbAnoPreA46ActionPerformed
     *
     * Finalidade: Controla a visibilidade do componente de observação
     * `jspAnoPreA46 com base na seleção do botão de alternância `jtbAnoPreA46`.
     *
     * Funcionalidades: - Se o botão estiver selecionado, torna o campo de
     * observação visível e define seu tamanho. - Se não estiver selecionado,
     * oculta o campo de observação.
     *
     * Observações: - Esse comportamento permite ao usuário expandir ou recolher
     * a área de anotação do item A46 da preventiva. - O tamanho fixo de 170x100
     * pode ser ajustado conforme a necessidade da interface.
     *
     * @param evt evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA46ActionPerformed

        if (jtbAnoPreA46.isSelected()) {
            jspAnoPreA46.setVisible(true);
            jspAnoPreA46.setSize(170, 100);
        } else {
            jspAnoPreA46.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA46ActionPerformed

    /**
     * Método jtbAnoPreA47ActionPerformed
     *
     * Finalidade: Controla a visibilidade do componente de observação
     * `jspAnoPreA47 com base na seleção do botão de alternância `jtbAnoPreA47`.
     *
     * Funcionalidades: - Se o botão estiver selecionado, torna o campo de
     * observação visível e define seu tamanho. - Se não estiver selecionado,
     * oculta o campo de observação.
     *
     * Observações: - Esse comportamento permite ao usuário expandir ou recolher
     * a área de anotação do item A47 da preventiva. - O tamanho fixo de 170x100
     * pode ser ajustado conforme a necessidade da interface.
     *
     * @param evt evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA47ActionPerformed

        if (jtbAnoPreA47.isSelected()) {
            jspAnoPreA47.setVisible(true);
            jspAnoPreA47.setSize(170, 100);
        } else {
            jspAnoPreA47.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA47ActionPerformed

    /**
     * Método jtbAnoPreA48ActionPerformed
     *
     * Finalidade: Controla a visibilidade do componente de observação
     * `jspAnoPreA48 com base na seleção do botão de alternância `jtbAnoPreA48`.
     *
     * Funcionalidades: - Se o botão estiver selecionado, torna o campo de
     * observação visível e define seu tamanho. - Se não estiver selecionado,
     * oculta o campo de observação.
     *
     * Observações: - Esse comportamento permite ao usuário expandir ou recolher
     * a área de anotação do item A48 da preventiva. - O tamanho fixo de 170x100
     * pode ser ajustado conforme a necessidade da interface.
     *
     * @param evt evento gerado pelo clique no botão de alternância
     */
    private void jtbAnoPreA48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbAnoPreA48ActionPerformed

        if (jtbAnoPreA48.isSelected()) {
            jspAnoPreA48.setVisible(true);
            jspAnoPreA48.setSize(170, 100);
        } else {
            jspAnoPreA48.setVisible(false);
        }
    }//GEN-LAST:event_jtbAnoPreA48ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFotPreA1;
    private javax.swing.JButton btnFotPreA10;
    private javax.swing.JButton btnFotPreA100;
    private javax.swing.JButton btnFotPreA101;
    private javax.swing.JButton btnFotPreA102;
    private javax.swing.JButton btnFotPreA103;
    private javax.swing.JButton btnFotPreA104;
    private javax.swing.JButton btnFotPreA105;
    private javax.swing.JButton btnFotPreA106;
    private javax.swing.JButton btnFotPreA107;
    private javax.swing.JButton btnFotPreA108;
    private javax.swing.JButton btnFotPreA109;
    private javax.swing.JButton btnFotPreA11;
    private javax.swing.JButton btnFotPreA110;
    private javax.swing.JButton btnFotPreA111;
    private javax.swing.JButton btnFotPreA112;
    private javax.swing.JButton btnFotPreA113;
    private javax.swing.JButton btnFotPreA114;
    private javax.swing.JButton btnFotPreA115;
    private javax.swing.JButton btnFotPreA116;
    private javax.swing.JButton btnFotPreA117;
    private javax.swing.JButton btnFotPreA118;
    private javax.swing.JButton btnFotPreA12;
    private javax.swing.JButton btnFotPreA13;
    private javax.swing.JButton btnFotPreA14;
    private javax.swing.JButton btnFotPreA15;
    private javax.swing.JButton btnFotPreA16;
    private javax.swing.JButton btnFotPreA17;
    private javax.swing.JButton btnFotPreA18;
    private javax.swing.JButton btnFotPreA19;
    private javax.swing.JButton btnFotPreA2;
    private javax.swing.JButton btnFotPreA20;
    private javax.swing.JButton btnFotPreA21;
    private javax.swing.JButton btnFotPreA22;
    private javax.swing.JButton btnFotPreA23;
    private javax.swing.JButton btnFotPreA24;
    private javax.swing.JButton btnFotPreA25;
    private javax.swing.JButton btnFotPreA26;
    private javax.swing.JButton btnFotPreA27;
    private javax.swing.JButton btnFotPreA28;
    private javax.swing.JButton btnFotPreA29;
    private javax.swing.JButton btnFotPreA3;
    private javax.swing.JButton btnFotPreA30;
    private javax.swing.JButton btnFotPreA31;
    private javax.swing.JButton btnFotPreA32;
    private javax.swing.JButton btnFotPreA33;
    private javax.swing.JButton btnFotPreA34;
    private javax.swing.JButton btnFotPreA35;
    private javax.swing.JButton btnFotPreA36;
    private javax.swing.JButton btnFotPreA37;
    private javax.swing.JButton btnFotPreA38;
    private javax.swing.JButton btnFotPreA39;
    private javax.swing.JButton btnFotPreA4;
    private javax.swing.JButton btnFotPreA40;
    private javax.swing.JButton btnFotPreA41;
    private javax.swing.JButton btnFotPreA42;
    private javax.swing.JButton btnFotPreA43;
    private javax.swing.JButton btnFotPreA44;
    private javax.swing.JButton btnFotPreA45;
    private javax.swing.JButton btnFotPreA46;
    private javax.swing.JButton btnFotPreA47;
    private javax.swing.JButton btnFotPreA48;
    private javax.swing.JButton btnFotPreA49;
    private javax.swing.JButton btnFotPreA5;
    private javax.swing.JButton btnFotPreA50;
    private javax.swing.JButton btnFotPreA51;
    private javax.swing.JButton btnFotPreA52;
    private javax.swing.JButton btnFotPreA53;
    private javax.swing.JButton btnFotPreA54;
    private javax.swing.JButton btnFotPreA55;
    private javax.swing.JButton btnFotPreA56;
    private javax.swing.JButton btnFotPreA57;
    private javax.swing.JButton btnFotPreA58;
    private javax.swing.JButton btnFotPreA59;
    private javax.swing.JButton btnFotPreA6;
    private javax.swing.JButton btnFotPreA60;
    private javax.swing.JButton btnFotPreA61;
    private javax.swing.JButton btnFotPreA62;
    private javax.swing.JButton btnFotPreA63;
    private javax.swing.JButton btnFotPreA64;
    private javax.swing.JButton btnFotPreA65;
    private javax.swing.JButton btnFotPreA66;
    private javax.swing.JButton btnFotPreA67;
    private javax.swing.JButton btnFotPreA68;
    private javax.swing.JButton btnFotPreA69;
    private javax.swing.JButton btnFotPreA7;
    private javax.swing.JButton btnFotPreA70;
    private javax.swing.JButton btnFotPreA71;
    private javax.swing.JButton btnFotPreA72;
    private javax.swing.JButton btnFotPreA73;
    private javax.swing.JButton btnFotPreA74;
    private javax.swing.JButton btnFotPreA75;
    private javax.swing.JButton btnFotPreA76;
    private javax.swing.JButton btnFotPreA77;
    private javax.swing.JButton btnFotPreA78;
    private javax.swing.JButton btnFotPreA79;
    private javax.swing.JButton btnFotPreA8;
    private javax.swing.JButton btnFotPreA80;
    private javax.swing.JButton btnFotPreA81;
    private javax.swing.JButton btnFotPreA82;
    private javax.swing.JButton btnFotPreA83;
    private javax.swing.JButton btnFotPreA84;
    private javax.swing.JButton btnFotPreA85;
    private javax.swing.JButton btnFotPreA86;
    private javax.swing.JButton btnFotPreA87;
    private javax.swing.JButton btnFotPreA88;
    private javax.swing.JButton btnFotPreA89;
    private javax.swing.JButton btnFotPreA9;
    private javax.swing.JButton btnFotPreA90;
    private javax.swing.JButton btnFotPreA91;
    private javax.swing.JButton btnFotPreA92;
    private javax.swing.JButton btnFotPreA93;
    private javax.swing.JButton btnFotPreA94;
    private javax.swing.JButton btnFotPreA95;
    private javax.swing.JButton btnFotPreA96;
    private javax.swing.JButton btnFotPreA97;
    private javax.swing.JButton btnFotPreA98;
    private javax.swing.JButton btnFotPreA99;
    private javax.swing.JButton btnImpPre;
    private javax.swing.JButton btnImpPre1;
    private javax.swing.JButton btnImpPre2;
    public static javax.swing.JButton btnSalFotPre8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel jpaAba1;
    private javax.swing.JPanel jpaAba2;
    private javax.swing.JPanel jpaAba3;
    public static javax.swing.JPanel jpaArqPre;
    private javax.swing.JScrollPane jspAnoPreA1;
    private javax.swing.JScrollPane jspAnoPreA10;
    private javax.swing.JScrollPane jspAnoPreA100;
    private javax.swing.JScrollPane jspAnoPreA101;
    private javax.swing.JScrollPane jspAnoPreA102;
    private javax.swing.JScrollPane jspAnoPreA103;
    private javax.swing.JScrollPane jspAnoPreA104;
    private javax.swing.JScrollPane jspAnoPreA105;
    private javax.swing.JScrollPane jspAnoPreA106;
    private javax.swing.JScrollPane jspAnoPreA107;
    private javax.swing.JScrollPane jspAnoPreA108;
    private javax.swing.JScrollPane jspAnoPreA109;
    private javax.swing.JScrollPane jspAnoPreA11;
    private javax.swing.JScrollPane jspAnoPreA110;
    private javax.swing.JScrollPane jspAnoPreA111;
    private javax.swing.JScrollPane jspAnoPreA112;
    private javax.swing.JScrollPane jspAnoPreA113;
    private javax.swing.JScrollPane jspAnoPreA114;
    private javax.swing.JScrollPane jspAnoPreA115;
    private javax.swing.JScrollPane jspAnoPreA116;
    private javax.swing.JScrollPane jspAnoPreA117;
    private javax.swing.JScrollPane jspAnoPreA118;
    private javax.swing.JScrollPane jspAnoPreA12;
    private javax.swing.JScrollPane jspAnoPreA13;
    private javax.swing.JScrollPane jspAnoPreA14;
    private javax.swing.JScrollPane jspAnoPreA15;
    private javax.swing.JScrollPane jspAnoPreA16;
    private javax.swing.JScrollPane jspAnoPreA17;
    private javax.swing.JScrollPane jspAnoPreA18;
    private javax.swing.JScrollPane jspAnoPreA19;
    private javax.swing.JScrollPane jspAnoPreA2;
    private javax.swing.JScrollPane jspAnoPreA20;
    private javax.swing.JScrollPane jspAnoPreA21;
    private javax.swing.JScrollPane jspAnoPreA22;
    private javax.swing.JScrollPane jspAnoPreA23;
    private javax.swing.JScrollPane jspAnoPreA24;
    private javax.swing.JScrollPane jspAnoPreA25;
    private javax.swing.JScrollPane jspAnoPreA26;
    private javax.swing.JScrollPane jspAnoPreA27;
    private javax.swing.JScrollPane jspAnoPreA28;
    private javax.swing.JScrollPane jspAnoPreA29;
    private javax.swing.JScrollPane jspAnoPreA3;
    private javax.swing.JScrollPane jspAnoPreA30;
    private javax.swing.JScrollPane jspAnoPreA31;
    private javax.swing.JScrollPane jspAnoPreA32;
    private javax.swing.JScrollPane jspAnoPreA33;
    private javax.swing.JScrollPane jspAnoPreA34;
    private javax.swing.JScrollPane jspAnoPreA35;
    private javax.swing.JScrollPane jspAnoPreA36;
    private javax.swing.JScrollPane jspAnoPreA37;
    private javax.swing.JScrollPane jspAnoPreA38;
    private javax.swing.JScrollPane jspAnoPreA39;
    private javax.swing.JScrollPane jspAnoPreA4;
    private javax.swing.JScrollPane jspAnoPreA40;
    private javax.swing.JScrollPane jspAnoPreA41;
    private javax.swing.JScrollPane jspAnoPreA42;
    private javax.swing.JScrollPane jspAnoPreA43;
    private javax.swing.JScrollPane jspAnoPreA44;
    private javax.swing.JScrollPane jspAnoPreA45;
    private javax.swing.JScrollPane jspAnoPreA46;
    private javax.swing.JScrollPane jspAnoPreA47;
    private javax.swing.JScrollPane jspAnoPreA48;
    private javax.swing.JScrollPane jspAnoPreA49;
    private javax.swing.JScrollPane jspAnoPreA5;
    private javax.swing.JScrollPane jspAnoPreA50;
    private javax.swing.JScrollPane jspAnoPreA51;
    private javax.swing.JScrollPane jspAnoPreA52;
    private javax.swing.JScrollPane jspAnoPreA53;
    private javax.swing.JScrollPane jspAnoPreA54;
    private javax.swing.JScrollPane jspAnoPreA55;
    private javax.swing.JScrollPane jspAnoPreA56;
    private javax.swing.JScrollPane jspAnoPreA57;
    private javax.swing.JScrollPane jspAnoPreA58;
    private javax.swing.JScrollPane jspAnoPreA59;
    private javax.swing.JScrollPane jspAnoPreA6;
    private javax.swing.JScrollPane jspAnoPreA60;
    private javax.swing.JScrollPane jspAnoPreA61;
    private javax.swing.JScrollPane jspAnoPreA62;
    private javax.swing.JScrollPane jspAnoPreA63;
    private javax.swing.JScrollPane jspAnoPreA64;
    private javax.swing.JScrollPane jspAnoPreA65;
    private javax.swing.JScrollPane jspAnoPreA66;
    private javax.swing.JScrollPane jspAnoPreA67;
    private javax.swing.JScrollPane jspAnoPreA68;
    private javax.swing.JScrollPane jspAnoPreA69;
    private javax.swing.JScrollPane jspAnoPreA7;
    private javax.swing.JScrollPane jspAnoPreA70;
    private javax.swing.JScrollPane jspAnoPreA71;
    private javax.swing.JScrollPane jspAnoPreA72;
    private javax.swing.JScrollPane jspAnoPreA73;
    private javax.swing.JScrollPane jspAnoPreA74;
    private javax.swing.JScrollPane jspAnoPreA75;
    private javax.swing.JScrollPane jspAnoPreA76;
    private javax.swing.JScrollPane jspAnoPreA77;
    private javax.swing.JScrollPane jspAnoPreA78;
    private javax.swing.JScrollPane jspAnoPreA79;
    private javax.swing.JScrollPane jspAnoPreA8;
    private javax.swing.JScrollPane jspAnoPreA80;
    private javax.swing.JScrollPane jspAnoPreA81;
    private javax.swing.JScrollPane jspAnoPreA82;
    private javax.swing.JScrollPane jspAnoPreA83;
    private javax.swing.JScrollPane jspAnoPreA84;
    private javax.swing.JScrollPane jspAnoPreA85;
    private javax.swing.JScrollPane jspAnoPreA86;
    private javax.swing.JScrollPane jspAnoPreA87;
    private javax.swing.JScrollPane jspAnoPreA88;
    private javax.swing.JScrollPane jspAnoPreA89;
    private javax.swing.JScrollPane jspAnoPreA9;
    private javax.swing.JScrollPane jspAnoPreA90;
    private javax.swing.JScrollPane jspAnoPreA91;
    private javax.swing.JScrollPane jspAnoPreA92;
    private javax.swing.JScrollPane jspAnoPreA93;
    private javax.swing.JScrollPane jspAnoPreA94;
    private javax.swing.JScrollPane jspAnoPreA95;
    private javax.swing.JScrollPane jspAnoPreA96;
    private javax.swing.JScrollPane jspAnoPreA97;
    private javax.swing.JScrollPane jspAnoPreA98;
    private javax.swing.JScrollPane jspAnoPreA99;
    private javax.swing.JTextArea jtaAnoA1;
    private javax.swing.JTextArea jtaAnoA10;
    private javax.swing.JTextArea jtaAnoA100;
    private javax.swing.JTextArea jtaAnoA101;
    private javax.swing.JTextArea jtaAnoA102;
    private javax.swing.JTextArea jtaAnoA103;
    private javax.swing.JTextArea jtaAnoA104;
    private javax.swing.JTextArea jtaAnoA105;
    private javax.swing.JTextArea jtaAnoA106;
    private javax.swing.JTextArea jtaAnoA107;
    private javax.swing.JTextArea jtaAnoA108;
    private javax.swing.JTextArea jtaAnoA109;
    private javax.swing.JTextArea jtaAnoA11;
    private javax.swing.JTextArea jtaAnoA110;
    private javax.swing.JTextArea jtaAnoA111;
    private javax.swing.JTextArea jtaAnoA112;
    private javax.swing.JTextArea jtaAnoA113;
    private javax.swing.JTextArea jtaAnoA114;
    private javax.swing.JTextArea jtaAnoA115;
    private javax.swing.JTextArea jtaAnoA116;
    private javax.swing.JTextArea jtaAnoA117;
    private javax.swing.JTextArea jtaAnoA118;
    private javax.swing.JTextArea jtaAnoA12;
    private javax.swing.JTextArea jtaAnoA13;
    private javax.swing.JTextArea jtaAnoA14;
    private javax.swing.JTextArea jtaAnoA15;
    private javax.swing.JTextArea jtaAnoA16;
    private javax.swing.JTextArea jtaAnoA17;
    private javax.swing.JTextArea jtaAnoA18;
    private javax.swing.JTextArea jtaAnoA19;
    private javax.swing.JTextArea jtaAnoA2;
    private javax.swing.JTextArea jtaAnoA20;
    private javax.swing.JTextArea jtaAnoA21;
    private javax.swing.JTextArea jtaAnoA22;
    private javax.swing.JTextArea jtaAnoA23;
    private javax.swing.JTextArea jtaAnoA24;
    private javax.swing.JTextArea jtaAnoA25;
    private javax.swing.JTextArea jtaAnoA26;
    private javax.swing.JTextArea jtaAnoA27;
    private javax.swing.JTextArea jtaAnoA28;
    private javax.swing.JTextArea jtaAnoA29;
    private javax.swing.JTextArea jtaAnoA3;
    private javax.swing.JTextArea jtaAnoA30;
    private javax.swing.JTextArea jtaAnoA31;
    private javax.swing.JTextArea jtaAnoA32;
    private javax.swing.JTextArea jtaAnoA33;
    private javax.swing.JTextArea jtaAnoA34;
    private javax.swing.JTextArea jtaAnoA35;
    private javax.swing.JTextArea jtaAnoA36;
    private javax.swing.JTextArea jtaAnoA37;
    private javax.swing.JTextArea jtaAnoA38;
    private javax.swing.JTextArea jtaAnoA39;
    private javax.swing.JTextArea jtaAnoA4;
    private javax.swing.JTextArea jtaAnoA40;
    private javax.swing.JTextArea jtaAnoA41;
    private javax.swing.JTextArea jtaAnoA42;
    private javax.swing.JTextArea jtaAnoA43;
    private javax.swing.JTextArea jtaAnoA44;
    private javax.swing.JTextArea jtaAnoA45;
    private javax.swing.JTextArea jtaAnoA46;
    private javax.swing.JTextArea jtaAnoA47;
    private javax.swing.JTextArea jtaAnoA48;
    private javax.swing.JTextArea jtaAnoA49;
    private javax.swing.JTextArea jtaAnoA5;
    private javax.swing.JTextArea jtaAnoA50;
    private javax.swing.JTextArea jtaAnoA51;
    private javax.swing.JTextArea jtaAnoA52;
    private javax.swing.JTextArea jtaAnoA53;
    private javax.swing.JTextArea jtaAnoA54;
    private javax.swing.JTextArea jtaAnoA55;
    private javax.swing.JTextArea jtaAnoA56;
    private javax.swing.JTextArea jtaAnoA57;
    private javax.swing.JTextArea jtaAnoA58;
    private javax.swing.JTextArea jtaAnoA59;
    private javax.swing.JTextArea jtaAnoA6;
    private javax.swing.JTextArea jtaAnoA60;
    private javax.swing.JTextArea jtaAnoA61;
    private javax.swing.JTextArea jtaAnoA62;
    private javax.swing.JTextArea jtaAnoA63;
    private javax.swing.JTextArea jtaAnoA64;
    private javax.swing.JTextArea jtaAnoA65;
    private javax.swing.JTextArea jtaAnoA66;
    private javax.swing.JTextArea jtaAnoA67;
    private javax.swing.JTextArea jtaAnoA68;
    private javax.swing.JTextArea jtaAnoA69;
    private javax.swing.JTextArea jtaAnoA7;
    private javax.swing.JTextArea jtaAnoA70;
    private javax.swing.JTextArea jtaAnoA71;
    private javax.swing.JTextArea jtaAnoA72;
    private javax.swing.JTextArea jtaAnoA73;
    private javax.swing.JTextArea jtaAnoA74;
    private javax.swing.JTextArea jtaAnoA75;
    private javax.swing.JTextArea jtaAnoA76;
    private javax.swing.JTextArea jtaAnoA77;
    private javax.swing.JTextArea jtaAnoA78;
    private javax.swing.JTextArea jtaAnoA79;
    private javax.swing.JTextArea jtaAnoA8;
    private javax.swing.JTextArea jtaAnoA80;
    private javax.swing.JTextArea jtaAnoA81;
    private javax.swing.JTextArea jtaAnoA82;
    private javax.swing.JTextArea jtaAnoA83;
    private javax.swing.JTextArea jtaAnoA84;
    private javax.swing.JTextArea jtaAnoA85;
    private javax.swing.JTextArea jtaAnoA86;
    private javax.swing.JTextArea jtaAnoA87;
    private javax.swing.JTextArea jtaAnoA88;
    private javax.swing.JTextArea jtaAnoA89;
    private javax.swing.JTextArea jtaAnoA9;
    private javax.swing.JTextArea jtaAnoA90;
    private javax.swing.JTextArea jtaAnoA91;
    private javax.swing.JTextArea jtaAnoA92;
    private javax.swing.JTextArea jtaAnoA93;
    private javax.swing.JTextArea jtaAnoA94;
    private javax.swing.JTextArea jtaAnoA95;
    private javax.swing.JTextArea jtaAnoA96;
    private javax.swing.JTextArea jtaAnoA97;
    private javax.swing.JTextArea jtaAnoA98;
    private javax.swing.JTextArea jtaAnoA99;
    private javax.swing.JTextArea jtaEquPre;
    private javax.swing.JTextArea jtaEquPre1;
    private javax.swing.JTextArea jtaEquPre2;
    private javax.swing.JTextArea jtaSetPre;
    private javax.swing.JTextArea jtaSetPre1;
    private javax.swing.JTextArea jtaSetPre2;
    private javax.swing.JToggleButton jtbAnoPreA1;
    private javax.swing.JToggleButton jtbAnoPreA10;
    private javax.swing.JToggleButton jtbAnoPreA100;
    private javax.swing.JToggleButton jtbAnoPreA101;
    private javax.swing.JToggleButton jtbAnoPreA102;
    private javax.swing.JToggleButton jtbAnoPreA103;
    private javax.swing.JToggleButton jtbAnoPreA104;
    private javax.swing.JToggleButton jtbAnoPreA105;
    private javax.swing.JToggleButton jtbAnoPreA106;
    private javax.swing.JToggleButton jtbAnoPreA107;
    private javax.swing.JToggleButton jtbAnoPreA108;
    private javax.swing.JToggleButton jtbAnoPreA109;
    private javax.swing.JToggleButton jtbAnoPreA11;
    private javax.swing.JToggleButton jtbAnoPreA110;
    private javax.swing.JToggleButton jtbAnoPreA111;
    private javax.swing.JToggleButton jtbAnoPreA112;
    private javax.swing.JToggleButton jtbAnoPreA113;
    private javax.swing.JToggleButton jtbAnoPreA114;
    private javax.swing.JToggleButton jtbAnoPreA115;
    private javax.swing.JToggleButton jtbAnoPreA116;
    private javax.swing.JToggleButton jtbAnoPreA117;
    private javax.swing.JToggleButton jtbAnoPreA118;
    private javax.swing.JToggleButton jtbAnoPreA12;
    private javax.swing.JToggleButton jtbAnoPreA13;
    private javax.swing.JToggleButton jtbAnoPreA14;
    private javax.swing.JToggleButton jtbAnoPreA15;
    private javax.swing.JToggleButton jtbAnoPreA16;
    private javax.swing.JToggleButton jtbAnoPreA17;
    private javax.swing.JToggleButton jtbAnoPreA18;
    private javax.swing.JToggleButton jtbAnoPreA19;
    private javax.swing.JToggleButton jtbAnoPreA2;
    private javax.swing.JToggleButton jtbAnoPreA20;
    private javax.swing.JToggleButton jtbAnoPreA21;
    private javax.swing.JToggleButton jtbAnoPreA22;
    private javax.swing.JToggleButton jtbAnoPreA23;
    private javax.swing.JToggleButton jtbAnoPreA24;
    private javax.swing.JToggleButton jtbAnoPreA25;
    private javax.swing.JToggleButton jtbAnoPreA26;
    private javax.swing.JToggleButton jtbAnoPreA27;
    private javax.swing.JToggleButton jtbAnoPreA28;
    private javax.swing.JToggleButton jtbAnoPreA29;
    private javax.swing.JToggleButton jtbAnoPreA3;
    private javax.swing.JToggleButton jtbAnoPreA30;
    private javax.swing.JToggleButton jtbAnoPreA31;
    private javax.swing.JToggleButton jtbAnoPreA32;
    private javax.swing.JToggleButton jtbAnoPreA33;
    private javax.swing.JToggleButton jtbAnoPreA34;
    private javax.swing.JToggleButton jtbAnoPreA35;
    private javax.swing.JToggleButton jtbAnoPreA36;
    private javax.swing.JToggleButton jtbAnoPreA37;
    private javax.swing.JToggleButton jtbAnoPreA38;
    private javax.swing.JToggleButton jtbAnoPreA39;
    private javax.swing.JToggleButton jtbAnoPreA4;
    private javax.swing.JToggleButton jtbAnoPreA40;
    private javax.swing.JToggleButton jtbAnoPreA41;
    private javax.swing.JToggleButton jtbAnoPreA42;
    private javax.swing.JToggleButton jtbAnoPreA43;
    private javax.swing.JToggleButton jtbAnoPreA44;
    private javax.swing.JToggleButton jtbAnoPreA45;
    private javax.swing.JToggleButton jtbAnoPreA46;
    private javax.swing.JToggleButton jtbAnoPreA47;
    private javax.swing.JToggleButton jtbAnoPreA48;
    private javax.swing.JToggleButton jtbAnoPreA49;
    private javax.swing.JToggleButton jtbAnoPreA5;
    private javax.swing.JToggleButton jtbAnoPreA50;
    private javax.swing.JToggleButton jtbAnoPreA51;
    private javax.swing.JToggleButton jtbAnoPreA52;
    private javax.swing.JToggleButton jtbAnoPreA53;
    private javax.swing.JToggleButton jtbAnoPreA54;
    private javax.swing.JToggleButton jtbAnoPreA55;
    private javax.swing.JToggleButton jtbAnoPreA56;
    private javax.swing.JToggleButton jtbAnoPreA57;
    private javax.swing.JToggleButton jtbAnoPreA58;
    private javax.swing.JToggleButton jtbAnoPreA59;
    private javax.swing.JToggleButton jtbAnoPreA6;
    private javax.swing.JToggleButton jtbAnoPreA60;
    private javax.swing.JToggleButton jtbAnoPreA61;
    private javax.swing.JToggleButton jtbAnoPreA62;
    private javax.swing.JToggleButton jtbAnoPreA63;
    private javax.swing.JToggleButton jtbAnoPreA64;
    private javax.swing.JToggleButton jtbAnoPreA65;
    private javax.swing.JToggleButton jtbAnoPreA66;
    private javax.swing.JToggleButton jtbAnoPreA67;
    private javax.swing.JToggleButton jtbAnoPreA68;
    private javax.swing.JToggleButton jtbAnoPreA69;
    private javax.swing.JToggleButton jtbAnoPreA7;
    private javax.swing.JToggleButton jtbAnoPreA70;
    private javax.swing.JToggleButton jtbAnoPreA71;
    private javax.swing.JToggleButton jtbAnoPreA72;
    private javax.swing.JToggleButton jtbAnoPreA73;
    private javax.swing.JToggleButton jtbAnoPreA74;
    private javax.swing.JToggleButton jtbAnoPreA75;
    private javax.swing.JToggleButton jtbAnoPreA76;
    private javax.swing.JToggleButton jtbAnoPreA77;
    private javax.swing.JToggleButton jtbAnoPreA78;
    private javax.swing.JToggleButton jtbAnoPreA79;
    private javax.swing.JToggleButton jtbAnoPreA8;
    private javax.swing.JToggleButton jtbAnoPreA80;
    private javax.swing.JToggleButton jtbAnoPreA81;
    private javax.swing.JToggleButton jtbAnoPreA82;
    private javax.swing.JToggleButton jtbAnoPreA83;
    private javax.swing.JToggleButton jtbAnoPreA84;
    private javax.swing.JToggleButton jtbAnoPreA85;
    private javax.swing.JToggleButton jtbAnoPreA86;
    private javax.swing.JToggleButton jtbAnoPreA87;
    private javax.swing.JToggleButton jtbAnoPreA88;
    private javax.swing.JToggleButton jtbAnoPreA89;
    private javax.swing.JToggleButton jtbAnoPreA9;
    private javax.swing.JToggleButton jtbAnoPreA90;
    private javax.swing.JToggleButton jtbAnoPreA91;
    private javax.swing.JToggleButton jtbAnoPreA92;
    private javax.swing.JToggleButton jtbAnoPreA93;
    private javax.swing.JToggleButton jtbAnoPreA94;
    private javax.swing.JToggleButton jtbAnoPreA95;
    private javax.swing.JToggleButton jtbAnoPreA96;
    private javax.swing.JToggleButton jtbAnoPreA97;
    private javax.swing.JToggleButton jtbAnoPreA98;
    private javax.swing.JToggleButton jtbAnoPreA99;
    public static javax.swing.JTabbedPane jtpAbas;
    public static javax.swing.JLabel lblFotEltMen8;
    private javax.swing.JTable tblArqPre;
    private javax.swing.JButton txtCanFot;
    private javax.swing.JTextField txtCodEqu;
    private javax.swing.JTextField txtCodEqu1;
    private javax.swing.JTextField txtCodEqu2;
    private javax.swing.JTextField txtDatFin;
    private javax.swing.JTextField txtDatFin1;
    private javax.swing.JTextField txtDatFin2;
    private javax.swing.JTextField txtDatIni;
    private javax.swing.JTextField txtDatIni1;
    private javax.swing.JTextField txtDatIni2;
    private javax.swing.JTextField txtDatPre;
    private javax.swing.JTextField txtDatPre1;
    private javax.swing.JTextField txtDatPre2;
    private javax.swing.JTextField txtFotPre8;
    private javax.swing.JTextField txtIdPre;
    private javax.swing.JTextField txtIdPre1;
    private javax.swing.JTextField txtIdPre2;
    private javax.swing.JTextField txtNomTec;
    private javax.swing.JTextField txtNomTec1;
    private javax.swing.JTextField txtNomTec2;
    private javax.swing.JTextField txtPesArq;
    private javax.swing.JTextField txtPreA1;
    private javax.swing.JTextField txtPreA10;
    private javax.swing.JTextField txtPreA100;
    private javax.swing.JTextField txtPreA101;
    private javax.swing.JTextField txtPreA102;
    private javax.swing.JTextField txtPreA103;
    private javax.swing.JTextField txtPreA104;
    private javax.swing.JTextField txtPreA105;
    private javax.swing.JTextField txtPreA106;
    private javax.swing.JTextField txtPreA107;
    private javax.swing.JTextField txtPreA108;
    private javax.swing.JTextField txtPreA109;
    private javax.swing.JTextField txtPreA11;
    private javax.swing.JTextField txtPreA110;
    private javax.swing.JTextField txtPreA111;
    private javax.swing.JTextField txtPreA112;
    private javax.swing.JTextField txtPreA113;
    private javax.swing.JTextField txtPreA114;
    private javax.swing.JTextField txtPreA115;
    private javax.swing.JTextField txtPreA116;
    private javax.swing.JTextField txtPreA117;
    private javax.swing.JTextField txtPreA118;
    private javax.swing.JTextField txtPreA12;
    private javax.swing.JTextField txtPreA13;
    private javax.swing.JTextField txtPreA14;
    private javax.swing.JTextField txtPreA15;
    private javax.swing.JTextField txtPreA16;
    private javax.swing.JTextField txtPreA17;
    private javax.swing.JTextField txtPreA18;
    private javax.swing.JTextField txtPreA19;
    private javax.swing.JTextField txtPreA2;
    private javax.swing.JTextField txtPreA20;
    private javax.swing.JTextField txtPreA21;
    private javax.swing.JTextField txtPreA22;
    private javax.swing.JTextField txtPreA23;
    private javax.swing.JTextField txtPreA24;
    private javax.swing.JTextField txtPreA25;
    private javax.swing.JTextField txtPreA26;
    private javax.swing.JTextField txtPreA27;
    private javax.swing.JTextField txtPreA28;
    private javax.swing.JTextField txtPreA29;
    private javax.swing.JTextField txtPreA3;
    private javax.swing.JTextField txtPreA30;
    private javax.swing.JTextField txtPreA31;
    private javax.swing.JTextField txtPreA32;
    private javax.swing.JTextField txtPreA33;
    private javax.swing.JTextField txtPreA34;
    private javax.swing.JTextField txtPreA35;
    private javax.swing.JTextField txtPreA36;
    private javax.swing.JTextField txtPreA37;
    private javax.swing.JTextField txtPreA38;
    private javax.swing.JTextField txtPreA39;
    private javax.swing.JTextField txtPreA4;
    private javax.swing.JTextField txtPreA40;
    private javax.swing.JTextField txtPreA41;
    private javax.swing.JTextField txtPreA42;
    private javax.swing.JTextField txtPreA43;
    private javax.swing.JTextField txtPreA44;
    private javax.swing.JTextField txtPreA45;
    private javax.swing.JTextField txtPreA46;
    private javax.swing.JTextField txtPreA47;
    private javax.swing.JTextField txtPreA48;
    private javax.swing.JTextField txtPreA49;
    private javax.swing.JTextField txtPreA5;
    private javax.swing.JTextField txtPreA50;
    private javax.swing.JTextField txtPreA51;
    private javax.swing.JTextField txtPreA52;
    private javax.swing.JTextField txtPreA53;
    private javax.swing.JTextField txtPreA54;
    private javax.swing.JTextField txtPreA55;
    private javax.swing.JTextField txtPreA56;
    private javax.swing.JTextField txtPreA57;
    private javax.swing.JTextField txtPreA58;
    private javax.swing.JTextField txtPreA59;
    private javax.swing.JTextField txtPreA6;
    private javax.swing.JTextField txtPreA60;
    private javax.swing.JTextField txtPreA61;
    private javax.swing.JTextField txtPreA62;
    private javax.swing.JTextField txtPreA63;
    private javax.swing.JTextField txtPreA64;
    private javax.swing.JTextField txtPreA65;
    private javax.swing.JTextField txtPreA66;
    private javax.swing.JTextField txtPreA67;
    private javax.swing.JTextField txtPreA68;
    private javax.swing.JTextField txtPreA69;
    private javax.swing.JTextField txtPreA7;
    private javax.swing.JTextField txtPreA70;
    private javax.swing.JTextField txtPreA71;
    private javax.swing.JTextField txtPreA72;
    private javax.swing.JTextField txtPreA73;
    private javax.swing.JTextField txtPreA74;
    private javax.swing.JTextField txtPreA75;
    private javax.swing.JTextField txtPreA76;
    private javax.swing.JTextField txtPreA77;
    private javax.swing.JTextField txtPreA78;
    private javax.swing.JTextField txtPreA79;
    private javax.swing.JTextField txtPreA8;
    private javax.swing.JTextField txtPreA80;
    private javax.swing.JTextField txtPreA81;
    private javax.swing.JTextField txtPreA82;
    private javax.swing.JTextField txtPreA83;
    private javax.swing.JTextField txtPreA84;
    private javax.swing.JTextField txtPreA85;
    private javax.swing.JTextField txtPreA86;
    private javax.swing.JTextField txtPreA87;
    private javax.swing.JTextField txtPreA88;
    private javax.swing.JTextField txtPreA89;
    private javax.swing.JTextField txtPreA9;
    private javax.swing.JTextField txtPreA90;
    private javax.swing.JTextField txtPreA91;
    private javax.swing.JTextField txtPreA92;
    private javax.swing.JTextField txtPreA93;
    private javax.swing.JTextField txtPreA94;
    private javax.swing.JTextField txtPreA95;
    private javax.swing.JTextField txtPreA96;
    private javax.swing.JTextField txtPreA97;
    private javax.swing.JTextField txtPreA98;
    private javax.swing.JTextField txtPreA99;
    private javax.swing.JTextField txtQanArq;
    private javax.swing.JTextField txtStaPre;
    private javax.swing.JTextField txtStaPre1;
    private javax.swing.JTextField txtStaPre2;
    private javax.swing.JTextField txtTemPre;
    private javax.swing.JTextField txtTemPre1;
    private javax.swing.JTextField txtTemPre2;
    // End of variables declaration//GEN-END:variables
}
