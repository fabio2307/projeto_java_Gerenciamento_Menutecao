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

import java.sql.*;
import com.prjmanutencao.dal.ModuloConexao;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.security.SecureRandom;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;

/**
 * Realiza o cadastro de setores e equipamentos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaSetor extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Construtor `TelaSetor`. Responsável por inicializar os componentes da
     * interface gráfica, estabelecer a conexão com o banco de dados e realizar
     * uma contagem inicial de itens ou setores.
     */
    public TelaSetor() {
        initComponents();
        conexao = ModuloConexao.conector();
        contagem();

    }

    /**
     * Classe `DocumentSizeFilter` que estende `DocumentFilter` para implementar
     * restrições de tamanho máximo em documentos editáveis. Este filtro impede
     * que o número total de caracteres em um documento ultrapasse um limite
     * especificado (maxCharacters).
     */
    class DocumentSizeFilter extends DocumentFilter {

        int maxCharacters;
        boolean DEBUG = false;

        public DocumentSizeFilter(int maxChars) {
            maxCharacters = maxChars;
        }

        /**
         * Método `insertString` sobrescrito da classe `DocumentFilter`. Ele é
         * responsável por limitar a quantidade de caracteres que podem ser
         * inseridos em um documento. Caso o limite máximo de caracteres seja
         * excedido, a inserção é rejeitada e um alerta sonoro é emitido.
         *
         * @param fb Objeto `FilterBypass` que permite modificar diretamente o
         * conteúdo do documento.
         * @param offs Posição no documento onde o texto será inserido.
         * @param str Texto que está sendo inserido.
         * @param a Conjunto de atributos associados ao texto inserido.
         * @throws BadLocationException Exceção lançada se a posição onde o
         * texto seria inserido for inválida.
         */
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offs,
                String str, AttributeSet a)
                throws BadLocationException {
            if (DEBUG) {
                System.out.println("in DocumentSizeFilter's insertString method");
            }

            //Isto rejeita novas inserções de caracteres se
            //a string for muito grande. Outra opção seria
            //truncar a string inserida, de forma que seja
            //adicionado somente o necessário para atingir
            //o limite máximo permitido
            if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
                super.insertString(fb, offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        /**
         * Método sobrescrito `replace` da classe `DocumentFilter`. Este método
         * é utilizado para substituir caracteres no documento de forma
         * controlada, garantindo que a substituição respeite o limite máximo de
         * caracteres permitido.
         *
         * @param fb Objeto `FilterBypass` que permite modificar diretamente o
         * conteúdo do documento.
         * @param offs Posição inicial onde a substituição será realizada.
         * @param length Número de caracteres que serão substituídos.
         * @param str Texto que será inserido como substituto.
         * @param a Conjunto de atributos associados ao texto substituto.
         * @throws BadLocationException Exceção lançada caso a posição
         * especificada no documento seja inválida.
         */
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
            if (DEBUG) {
                System.out.println("in DocumentSizeFilter's replace method");
            }
            //Isto rejeita novas inserções de caracteres se
            //a string for muito grande. Outra opção seria
            //truncar a string inserida, de forma que seja
            //adicionado somente o necessário para atingir
            //o limite máximo permitido
            if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters) {
                super.replace(fb, offs, length, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }

    /**
     * Método `contagem`. Configura os documentos associados a diversos campos
     * de texto (`txtUsuId`, `txtUsoNom`, etc.), aplicando filtros de tamanho
     * máximo em cada campo. Isso garante que os usuários não possam inserir
     * mais caracteres do que o limite definido.
     */
    private void contagem() {
        DefaultStyledDocument doc, doc1;

        doc = new DefaultStyledDocument();
        doc1 = new DefaultStyledDocument();

        doc.setDocumentFilter(new DocumentSizeFilter(45));
        doc1.setDocumentFilter(new DocumentSizeFilter(35));

        txaSetor.setDocument(doc);
        txaNomEqui.setDocument(doc1);
    }

    /**
     * Método `alinhar`. Responsável por configurar a largura das colunas,
     * centralizar os dados e personalizar o alinhamento do cabeçalho da tabela
     * `tblPeUsu`. Garante uma apresentação visual adequada e alinhada para os
     * dados exibidos na interface.
     */
    private void alinhar() {

        int width = 100, width1 = 237, width2 = 422, width3 = 90, width4 = 178, width5 = 90, width6 = 90;

        JTableHeader header, header1;
        try {

            header = tblSetores.getTableHeader();
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblSetores.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblSetores.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblSetores.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblSetores.getColumnModel().getColumn(2).setPreferredWidth(width2);

            header1 = tblCodEquiSet.getTableHeader();
            DefaultTableCellRenderer centralizado1 = (DefaultTableCellRenderer) header1.getDefaultRenderer();
            centralizado1.setHorizontalAlignment(SwingConstants.CENTER);
            tblCodEquiSet.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblCodEquiSet.getColumnModel().getColumn(0).setPreferredWidth(width3);
            tblCodEquiSet.getColumnModel().getColumn(1).setPreferredWidth(width4);
            tblCodEquiSet.getColumnModel().getColumn(2).setPreferredWidth(width5);
            tblCodEquiSet.getColumnModel().getColumn(3).setPreferredWidth(width6);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblSetores.getColumnCount(); i++) {
                tblSetores.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
            centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblCodEquiSet.getColumnCount(); i++) {
                tblCodEquiSet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisar`. Executa uma consulta SQL para buscar informações
     * sobre setores (código, andar e setor) no banco de dados. A pesquisa é
     * baseada nos valores fornecidos no campo de texto `txtPesquisar`. O
     * resultado é exibido na tabela `tblSetores`.
     *
     * @throws SQLException Em caso de falha na execução da consulta SQL.
     */
    private void pesquisar() {
        String sql = "select id_setor as 'Código', andar as 'Andar', setor as 'Setor' from setor where id_setor like ? or andar like ? or setor like ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            pst.setString(2, txtPesquisar.getText() + "%");
            pst.setString(3, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblSetores.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }
    }

    /**
     * Método `pesquisar1`. Executa uma consulta SQL para buscar todas as
     * informações sobre setores (código, andar e setor) no banco de dados. Os
     * dados são exibidos na tabela `tblSetores`.
     *
     * @throws SQLException Em caso de falha na execução da consulta SQL.
     */
    private void pesquisar1() {
        String sql = "select id_setor as 'Código', andar as 'Andar', setor as 'Setor' from setor ";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblSetores.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }
    }

    /**
     * Método `setar_campos`. Configura os campos e componentes da interface
     * gráfica com base na linha selecionada na tabela `tblSetores`. Realiza
     * ajustes no estado de botões, ComboBoxes e áreas de texto e invoca métodos
     * auxiliares para realizar outras ações, como buscar equipamentos, contar
     * itens e alinhar a tabela.
     */
    private void setar_campos() {
        int setar = tblSetores.getSelectedRow();
        txtId.setText(tblSetores.getModel().getValueAt(setar, 0).toString());
        cboAndar.setSelectedItem(tblSetores.getModel().getValueAt(setar, 1).toString());
        txaSetor.setText(tblSetores.getModel().getValueAt(setar, 2).toString());

        btnSetCreate.setEnabled(false);
        btnSetDelete.setEnabled(true);
        btnSetUpdate.setEnabled(true);
        btnAltCod.setEnabled(false);
        btnModEqui.setEnabled(false);
        btnApaSet.setEnabled(false);
        cboTipoEquiSet.setEnabled(false);
        txaNomEqui.setText("");
        txtIdEqu.setText(null);
        txtEquSet.setText(null);
        txtCodEquSet.setText("");
        cboTipoEquiSet.setSelectedItem("Elétrica");
        pesquisar_equipamento1();
        qunt_equipamento();
        alinhar();

        if (!txtId.getText().equals("")) {
            txaNomEqui.setEditable(true);
            btnNovoCad.setEnabled(true);
        } else {
            txaNomEqui.setEditable(false);
        }

    }

    /**
     * Método `adicionar`. Realiza a inserção de um novo setor no banco de dados
     * com base nas informações fornecidas. Verifica se os campos obrigatórios
     * estão preenchidos antes de executar a inserção. Em caso de sucesso ou
     * erro, fornece feedback ao usuário e atualiza os componentes da interface.
     *
     * @throws SQLException Em caso de falha ao executar a consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void adicionar() {
        String sql = "insert into setor ( andar, setor) values(?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboAndar.getSelectedItem().toString());
            pst.setString(2, txaSetor.getText());

            if ((cboAndar.getSelectedItem().toString().isEmpty()) || (txaSetor.getText().isEmpty())) {
                alinhar();
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int Adicionar = pst.executeUpdate();

                if (Adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Setor Cadastrado com Sucesso!");

                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnAdcSet.setEnabled(false);
                    pesquisar1();
                    limpar();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnAdcSet.setEnabled(false);
                    pesquisar1();
                    limpar();
                    dispose();
                    conexao.close();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `alterar`. Responsável por atualizar informações de um setor no
     * banco de dados com base no identificador fornecido. Verifica se os campos
     * obrigatórios estão preenchidos antes de executar a atualização e fornece
     * feedback ao usuário.
     *
     * @throws SQLException Em caso de falha na execução da consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void alterar() {
        String sql = "update setor set andar=?, setor=? where id_setor=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboAndar.getSelectedItem().toString());
            pst.setString(2, txaSetor.getText());
            pst.setString(3, txtId.getText());

            if ((cboAndar.getSelectedItem().toString().isEmpty()) || (txaSetor.getText().isEmpty())) {
                alinhar();
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int alterar = pst.executeUpdate();

                if (alterar > 0) {
                    JOptionPane.showMessageDialog(null, "Dados Alterados com Sucesso!");
                    limpar();
                    pesquisar1();
                    alinhar();
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText(null);
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    btnSetUpdate.setEnabled(false);
                    btnAdcSet.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Alterar Dados!");
                    conexao.close();
                    btnSetUpdate.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnAdcSet.setEnabled(false);
                    limpar();
                    pesquisar1();
                    conexao.close();
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `remover`. Responsável por excluir um setor do banco de dados com
     * base em seu identificador (`id_setor`). Solicita confirmação ao usuário
     * antes de executar a operação. Após remover o setor, redefine os campos e
     * atualiza os componentes da interface.
     *
     * @throws SQLException Em caso de falha na execução da consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Remover esse Setor?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from setor where id_setor=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Setor Removido com Sucesso!");
                    limpar();
                    pesquisar1();
                    btnSetDelete.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnAdcSet.setEnabled(false);
                    conexao.close();
                    dispose();

                }

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método `limpar`. Redefine os campos e componentes da interface gráfica
     * relacionados aos setores e equipamentos, garantindo que estejam limpos e
     * em seus estados padrão. Utilizado para preparar a interface para novas
     * interações.
     */
    private void limpar() {
        txtPesquisar.setText(null);
        txtQuaSet.setText(null);
        txtId.setText(null);
        cboAndar.setSelectedItem("3° Subsolo");
        txaSetor.setText("");
        txtEquSet.setText("");
        txaNomEqui.setText("");
        btnAdcSet.setEnabled(false);
//        ((DefaultTableModel) tblSetores.getModel()).setRowCount(0);
        btnSetDelete.setEnabled(false);
        btnNovoCad.setEnabled(false);
        txaNomEqui.setEditable(false);

    }

    /**
     * Método `equipamento_setor_add`. Gera automaticamente um código único para
     * identificar um equipamento dentro de um setor. O código é composto por: -
     * Três caracteres alfanuméricos aleatórios (A-Z, a-z). - Um número
     * aleatório entre 1 e 300000.
     *
     * O resultado é exibido no campo de texto `txtCodEquSet`.
     */
    private void equipamento_setor_add() {
        int len = 3;
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
            txtCodEquSet.setText(sb.toString() + " " + valor);

        }
    }

    /**
     * Método `cod_equipamento_set`. Gera um código único para o equipamento
     * utilizando o método `equipamento_setor_add()` e realiza a inserção desse
     * equipamento em um setor no banco de dados. Verifica os campos
     * obrigatórios antes de executar a operação e fornece feedback ao usuário
     * após a execução.
     *
     * @throws SQLException Em caso de falha ao executar a consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void cod_equipamento_set() {
        equipamento_setor_add();
        String sql = "insert into equipamento_setor( cod_equi_set, nome_equi_set, tipo_equip, id_setor, setor) values (?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCodEquSet.getText());
            pst.setString(2, txaNomEqui.getText());
            pst.setString(3, cboTipoEquiSet.getSelectedItem().toString());
            pst.setString(4, txtId.getText());
            pst.setString(5, cboAndar.getSelectedItem().toString() + " " + txaSetor.getText());
            if ((cboAndar.getSelectedItem().toString().isEmpty()) || (txaSetor.getText().isEmpty()) || (cboTipoEquiSet.getSelectedItem().toString().isEmpty() || (txtCodEquSet.getText().isEmpty()) || (txaNomEqui.getText()).isEmpty())) {
                txtCodEquSet.setText(null);
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                alinhar();
            } else {
                int Adicionar = pst.executeUpdate();

                if (Adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Equipamento Cadastrado com Sucesso!");
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtQuaSet.setText(null);
                    txaNomEqui.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnAdcSet.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    btnSetDelete.setEnabled(false);
                    btnSetUpdate.setEnabled(false);
                    txtCodEquSet.setText("");
                    limpar();
                    pesquisar_equipamento1();
                    qunt_equipamento();
                    alinhar();

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Equipamento");
                    alinhar();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }

    }

    /**
     * Método `pesquisar_equipamento`. Realiza uma pesquisa no banco de dados
     * para encontrar equipamentos com base nos parâmetros fornecidos. Os
     * resultados são exibidos na tabela `tblCodEquiSet`.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisar_equipamento() {

        String sql = "select id_equi_set as 'N°.', nome_equi_set as 'Equipamento', cod_equi_set as 'Identificação', tipo_equip as 'Tipo' from equipamento_setor where id_equi_set like ? or id_setor like ? or cod_equi_set like ? or nome_equi_set like ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEquSet.getText() + "%");
            pst.setString(2, txtEquSet.getText() + "%");
            pst.setString(3, txtEquSet.getText() + "%");
            pst.setString(4, txtEquSet.getText() + "%");
            rs = pst.executeQuery();
            tblCodEquiSet.setModel(DbUtils.resultSetToTableModel(rs));
            btnNovoCad.setEnabled(true);
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }

    }

    /**
     * Método `pesquisar_set`. Pesquisa o identificador do setor relacionado a
     * um equipamento específico com base no `id_equi_set`. Atualiza os campos
     * da interface com os resultados encontrados.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisar_set() {

        String sql = "select id_setor from equipamento_setor where id_equi_set = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdEqu.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtId.setText(rs.getString(1));
                alinhar();
                setor_pesq();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }

    }

    /**
     * Método `pesquisar_equipamento1`. Realiza uma pesquisa no banco de dados
     * para listar todos os equipamentos associados a um determinado setor. Os
     * resultados são exibidos na tabela `tblCodEquiSet`.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void pesquisar_equipamento1() {

        String sql = "select id_equi_set as 'N°',nome_equi_set as 'Equipamento', cod_equi_set as 'Identificação', tipo_equip as 'Tipo' from equipamento_setor where id_setor=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            rs = pst.executeQuery();
            tblCodEquiSet.setModel(DbUtils.resultSetToTableModel(rs));
            alinhar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }

    }

    /**
     * Método `setor_pesq`. Pesquisa informações de um setor no banco de dados
     * com base no identificador (`id_setor`) fornecido. Atualiza os campos da
     * interface gráfica com os dados encontrados.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     */
    private void setor_pesq() {

        String sql = "select id_setor, andar, setor from setor where id_setor = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                cboAndar.setSelectedItem(rs.getString(2));
                txaSetor.setText(rs.getString(3));
                alinhar();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            alinhar();
        }
    }

    /**
     * Método `qunt_equipamento`. Calcula a quantidade de equipamentos
     * associados a um setor específico no banco de dados e exibe o resultado no
     * campo `txtQuaSet`.
     *
     * @throws SQLException Em caso de falha ao executar a consulta SQL.
     */
    private void qunt_equipamento() {

        String sql = "select count(*) from equipamento_setor  where id_setor = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaSet.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `setar_equip`. Configura os campos e componentes da interface
     * gráfica com base na linha selecionada na tabela `tblCodEquiSet`. Atualiza
     * os campos relacionados ao equipamento e setor, além de ajustar o estado
     * de botões e outros componentes. Invoca métodos auxiliares para buscar
     * informações adicionais e atualizar a interface.
     */
    private void setar_equip() {
        int setar = tblCodEquiSet.getSelectedRow();
        txtIdEqu.setText(tblCodEquiSet.getModel().getValueAt(setar, 0).toString());
        txaNomEqui.setText(tblCodEquiSet.getModel().getValueAt(setar, 1).toString());
        txtCodEquSet.setText(tblCodEquiSet.getModel().getValueAt(setar, 2).toString());
        cboTipoEquiSet.setSelectedItem(tblCodEquiSet.getModel().getValueAt(setar, 3).toString());
        setor_pesq();
        pesquisar_set();

        btnAltCod.setEnabled(true);
        btnApaSet.setEnabled(true);
        btnModEqui.setEnabled(true);
        btnAdcSet.setEnabled(false);
        cboTipoEquiSet.setEnabled(true);
        btnSetCreate.setEnabled(false);
        btnSetDelete.setEnabled(false);
        btnSetUpdate.setEnabled(false);
        txtEquSet.setText(null);
        qunt_equipamento();
        alinhar();

        if (!txaNomEqui.getText().equals("")) {
            txaNomEqui.setEditable(true);
        } else {
            txaNomEqui.setEditable(false);
        }

    }

    /**
     * Método `alterar_cod_equ`. Atualiza as informações de um equipamento no
     * banco de dados com base no identificador (`id_equi_set`) fornecido.
     * Verifica se os campos obrigatórios estão preenchidos antes de executar a
     * operação e fornece feedback ao usuário.
     *
     * @throws SQLException Em caso de falha ao executar a consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void alterar_cod_equ() {

        String sql = "update equipamento_setor set nome_equi_set = ?, cod_equi_set = ?, tipo_equip = ?, Id_Setor = ?, setor = ? where id_equi_set = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txaNomEqui.getText());
            pst.setString(2, txtCodEquSet.getText());
            pst.setString(3, cboTipoEquiSet.getSelectedItem().toString());
            pst.setString(4, txtId.getText());
            pst.setString(5, cboAndar.getSelectedItem().toString() + " " + txaSetor.getText());
            pst.setString(6, txtIdEqu.getText());

            if ((cboAndar.getSelectedItem().toString().isEmpty()) || (txaSetor.getText().isEmpty()) || (txtCodEquSet.getText().isEmpty()) || (txtIdEqu.getText().isEmpty() || (txaNomEqui.getText().isEmpty()))) {
                alinhar();
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int alterar = pst.executeUpdate();

                if (alterar > 0) {
                    JOptionPane.showMessageDialog(null, "Dados Alterados com Sucesso!");
                    txtEquSet.setText(null);
                    txtQuaSet.setText(null);
                    txtIdEqu.setText(null);
                    txtCodEquSet.setText("");
                    txaNomEqui.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnModEqui.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    btnSetDelete.setEnabled(false);
                    btnSetUpdate.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    cboTipoEquiSet.setEnabled(false);
                    alinhar();
                    limpar();
                    pesquisar_equipamento1();

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Alterar Dados!");
                    alinhar();
                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `apqgar_cod_equ`. Responsável por remover um equipamento do banco
     * de dados com base no identificador (`id_equi_set`) fornecido. Solicita
     * confirmação ao usuário antes de executar a exclusão. Após a remoção,
     * redefine os campos e atualiza os componentes da interface.
     *
     * @throws SQLException Em caso de erro na execução da consulta SQL.
     * @throws HeadlessException Em caso de erro relacionado ao ambiente
     * gráfico.
     */
    private void apqgar_cod_equ() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Remover esse Setor?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from equipamento_setor where id_equi_set = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdEqu.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Equipamento Removido com Sucesso!");
                    txtEquSet.setText(null);
                    txtIdEqu.setText(null);
                    txtQuaSet.setText(null);
                    txaNomEqui.setText("");
                    txtCodEquSet.setText("");
                    cboTipoEquiSet.setSelectedItem("Elétrica");
                    cboTipoEquiSet.setEnabled(false);
                    ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
                    btnSetCreate.setEnabled(true);
                    btnAltCod.setEnabled(false);
                    btnApaSet.setEnabled(false);
                    btnSetCreate.setEnabled(true);
                    btnSetDelete.setEnabled(false);
                    btnSetUpdate.setEnabled(false);
                    pesquisar_equipamento1();
                    qunt_equipamento();
                    alinhar();
                    limpar();

                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
                alinhar();
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

        txtPesquisar = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        btnSetCreate = new javax.swing.JButton();
        btnSetUpdate = new javax.swing.JButton();
        btnSetDelete = new javax.swing.JButton();
        cboAndar = new javax.swing.JComboBox<>();
        txtCodEquSet = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCodEquiSet = new javax.swing.JTable();
        txtEquSet = new javax.swing.JTextField();
        btnAdcSet = new javax.swing.JButton();
        btnApaSet = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtIdEqu = new javax.swing.JTextField();
        cboTipoEquiSet = new javax.swing.JComboBox<>();
        btnModEqui = new javax.swing.JButton();
        btnAltCod = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtQuaSet = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSetores = new javax.swing.JTable();
        btnNovoCad = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblBot1 = new com.prjmanutencao.telas.LabelPersonalizada();
        jLabel18 = new javax.swing.JLabel();
        lblBot = new com.prjmanutencao.telas.LabelPersonalizada();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaSetor = new javax.swing.JTextArea();
        lblBot3 = new com.prjmanutencao.telas.LabelPersonalizada();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaNomEqui = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        lblBot2 = new com.prjmanutencao.telas.LabelPersonalizada();
        lblSetor = new com.prjmanutencao.telas.LabelPersonalizada();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Setor");
        setPreferredSize(new java.awt.Dimension(1009, 672));
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

        txtPesquisar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
        });
        getContentPane().add(txtPesquisar);
        txtPesquisar.setBounds(260, 120, 371, 30);

        txtId.setEditable(false);
        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtId);
        txtId.setBounds(104, 326, 90, 28);

        btnSetCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48761_file_add_file_upload_add_upload.png"))); // NOI18N
        btnSetCreate.setToolTipText("Adicionar");
        btnSetCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSetCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetCreateActionPerformed(evt);
            }
        });
        getContentPane().add(btnSetCreate);
        btnSetCreate.setBounds(530, 540, 64, 64);

        btnSetUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48763_edit_edit_file_file.png"))); // NOI18N
        btnSetUpdate.setToolTipText("Alterar");
        btnSetUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSetUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnSetUpdate);
        btnSetUpdate.setBounds(650, 540, 64, 64);

        btnSetDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48762_delete_delete file_delete_remove_delete_file_remove_remove_file.png"))); // NOI18N
        btnSetDelete.setToolTipText("Remover");
        btnSetDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSetDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnSetDelete);
        btnSetDelete.setBounds(770, 540, 64, 64);

        cboAndar.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cboAndar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3° Subsolo", "2° Subsolo", "1° Subsolo", "Térreo", "1° Andar ", "2° Andar ", "3° Andar", "4° Andar", "5° Andar", "6° Andar", "7° Andar", "8° Andar", "9° Andar", "10° Andar", "11° Andar", "12° Andar", "13° Andar", "14° Andar", "15° Andar", "16° Andar ", "17° Andar ", "18° Andar", "19° Andar", "20° Andar", "21° Andar", "22° Andar", "23° Andar", "24° Andar", "25° Andar", "26° Andar", "27° Andar", "28° Andar", "29° Andar", "30° Anda" }));
        getContentPane().add(cboAndar);
        cboAndar.setBounds(215, 326, 180, 28);

        txtCodEquSet.setEditable(false);
        txtCodEquSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtCodEquSet);
        txtCodEquSet.setBounds(194, 476, 90, 28);

        jScrollPane2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tblCodEquiSet = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblCodEquiSet.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblCodEquiSet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "N°", "Equipamento ", "Identificação", "Tipo"
            }
        ));
        tblCodEquiSet.getTableHeader().setResizingAllowed(false);
        tblCodEquiSet.getTableHeader().setReorderingAllowed(false);
        tblCodEquiSet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCodEquiSetMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCodEquiSet);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(450, 364, 470, 100);

        txtEquSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEquSet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEquSetKeyReleased(evt);
            }
        });
        getContentPane().add(txtEquSet);
        txtEquSet.setBounds(510, 330, 152, 28);

        btnAdcSet.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdcSet.setText("Adicionar");
        btnAdcSet.setToolTipText("Adicionar Equipamento");
        btnAdcSet.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 204, 0), new java.awt.Color(0, 204, 0), new java.awt.Color(0, 204, 0), new java.awt.Color(0, 204, 0)));
        btnAdcSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdcSetActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdcSet);
        btnAdcSet.setBounds(470, 478, 90, 28);

        btnApaSet.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnApaSet.setText("Excluir");
        btnApaSet.setToolTipText("Apagar Equipamento");
        btnApaSet.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 0, 0), new java.awt.Color(255, 0, 0), new java.awt.Color(255, 0, 0), new java.awt.Color(255, 0, 0)));
        btnApaSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApaSetActionPerformed(evt);
            }
        });
        getContentPane().add(btnApaSet);
        btnApaSet.setBounds(700, 478, 90, 28);

        jLabel3.setBackground(new java.awt.Color(0, 0, 51));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("N°");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(84, 450, 90, 26);

        txtIdEqu.setEditable(false);
        txtIdEqu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdEqu);
        txtIdEqu.setBounds(84, 476, 90, 28);

        cboTipoEquiSet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elétrica", "Hidráulica ", "Civil", "Refrigeração" }));
        getContentPane().add(cboTipoEquiSet);
        cboTipoEquiSet.setBounds(304, 476, 110, 28);

        btnModEqui.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnModEqui.setMnemonic('M');
        btnModEqui.setText("Modificar");
        btnModEqui.setToolTipText("Modificar Equipamento");
        btnModEqui.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        btnModEqui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModEquiActionPerformed(evt);
            }
        });
        getContentPane().add(btnModEqui);
        btnModEqui.setBounds(585, 478, 90, 28);

        btnAltCod.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAltCod.setText("Gerar Cod.");
        btnAltCod.setToolTipText("Gerar Codigo");
        btnAltCod.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 153), new java.awt.Color(0, 0, 153), new java.awt.Color(0, 0, 153), new java.awt.Color(0, 0, 153)));
        btnAltCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltCodActionPerformed(evt);
            }
        });
        getContentPane().add(btnAltCod);
        btnAltCod.setBounds(810, 478, 90, 28);

        jLabel5.setBackground(new java.awt.Color(0, 0, 51));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Equipamento");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(81, 506, 330, 26);

        jLabel6.setBackground(new java.awt.Color(0, 0, 51));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Identificação");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(194, 450, 90, 26);

        jLabel4.setBackground(new java.awt.Color(0, 0, 51));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tipo");
        jLabel4.setToolTipText("");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(304, 450, 110, 26);

        txtQuaSet.setEditable(false);
        txtQuaSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtQuaSet);
        txtQuaSet.setBounds(800, 330, 90, 28);

        jLabel7.setBackground(new java.awt.Color(0, 0, 51));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Quantidade");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(800, 300, 90, 28);

        tblSetores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblSetores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Andar", "Setor"
            }
        ));
        tblSetores.getTableHeader().setResizingAllowed(false);
        tblSetores.getTableHeader().setReorderingAllowed(false);
        tblSetores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSetoresMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblSetores);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(110, 160, 780, 130);

        btnNovoCad.setText("Novo Cadastro");
        btnNovoCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoCadActionPerformed(evt);
            }
        });
        getContentPane().add(btnNovoCad);
        btnNovoCad.setBounds(120, 120, 120, 30);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 44)); // NOI18N
        jLabel8.setText("Cadastrar Setores e Equipamentos\n");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(110, 30, 730, 40);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        getContentPane().add(jLabel9);
        jLabel9.setBounds(840, 18, 64, 64);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(665, 330, 30, 28);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Máximo de 45 Caracteres");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(88, 426, 140, 13);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Setor");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(81, 356, 330, 26);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Pesquisar");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(510, 300, 152, 28);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        getContentPane().add(jLabel14);
        jLabel14.setBounds(640, 120, 28, 28);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Andar");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(215, 296, 180, 28);

        lblBot1.setBackground(new java.awt.Color(153, 153, 153));
        lblBot1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblBot1);
        lblBot1.setBounds(450, 530, 470, 84);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 51));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("Equipamento");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(450, 286, 70, 20);

        lblBot.setBackground(new java.awt.Color(102, 102, 102));
        lblBot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblBot);
        lblBot.setBounds(436, 296, 500, 230);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Código");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(104, 296, 90, 28);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Setor");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(74, 286, 40, 20);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Máximo de 35 Caracteres");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(88, 574, 140, 11);

        txaSetor.setColumns(20);
        txaSetor.setLineWrap(true);
        txaSetor.setRows(5);
        jScrollPane1.setViewportView(txaSetor);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(81, 384, 331, 42);

        lblBot3.setBackground(new java.awt.Color(102, 102, 102));
        lblBot3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblBot3);
        lblBot3.setBounds(62, 296, 368, 146);

        txaNomEqui.setColumns(20);
        txaNomEqui.setLineWrap(true);
        txaNomEqui.setRows(5);
        txaNomEqui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaNomEquiKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(txaNomEqui);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(81, 531, 331, 42);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 51));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("Equipamento");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(74, 435, 70, 20);

        lblBot2.setBackground(new java.awt.Color(153, 153, 153));
        lblBot2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblBot2);
        lblBot2.setBounds(62, 444, 368, 146);
        getContentPane().add(lblSetor);
        lblSetor.setBounds(40, 100, 916, 530);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Os0.jpg"))); // NOI18N
        jLabel1.setToolTipText("");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(-1, 0, 1000, 672);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnSetCreateActionPerformed`. Manipula o evento de clique no
     * botão "Criar Setor". Invoca o método `adicionar()` para realizar o
     * cadastro de um novo setor no banco de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnSetCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetCreateActionPerformed

        adicionar();

    }//GEN-LAST:event_btnSetCreateActionPerformed

    /**
     * Método `btnSetUpdateActionPerformed`. Manipula o evento de clique no
     * botão "Atualizar Setor". Invoca o método `alterar()` para atualizar as
     * informações de um setor no banco de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnSetUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetUpdateActionPerformed

        alterar();

    }//GEN-LAST:event_btnSetUpdateActionPerformed

    /**
     * Método `btnSetDeleteActionPerformed`. Manipula o evento de clique no
     * botão "Remover Setor". Invoca o método `remover()` para executar a
     * exclusão de um setor do banco de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnSetDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDeleteActionPerformed

        remover();

    }//GEN-LAST:event_btnSetDeleteActionPerformed

    /**
     * Método `txtPesquisarKeyReleased`. Manipula o evento de digitação no campo
     * de texto `txtPesquisar`. Invoca o método `pesquisar()` sempre que uma
     * tecla for liberada enquanto o campo estiver ativo.
     *
     * @param evt Evento acionado pela liberação de uma tecla.
     */
    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased

        pesquisar();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    /**
     * Método `txtEquSetKeyReleased`. Manipula o evento de liberação de uma
     * tecla no campo de texto `txtEquSet`. Invoca o método
     * `pesquisar_equipamento()` para realizar a busca de equipamentos
     * dinamicamente conforme o usuário digita.
     *
     * @param evt Evento acionado pela liberação de uma tecla.
     */
    private void txtEquSetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEquSetKeyReleased

        pesquisar_equipamento();
    }//GEN-LAST:event_txtEquSetKeyReleased

    /**
     * Método `tblCodEquiSetMouseClicked`. Manipula o evento de clique em uma
     * linha da tabela `tblCodEquiSet`. Invoca o método `setar_equip()` para
     * configurar os campos e componentes da interface gráfica com base nos
     * dados da linha selecionada.
     *
     * @param evt Evento acionado pelo clique em uma linha da tabela.
     */
    private void tblCodEquiSetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCodEquiSetMouseClicked

        setar_equip();
    }//GEN-LAST:event_tblCodEquiSetMouseClicked

    /**
     * Método `btnAdcSetActionPerformed`. Manipula o evento de clique no botão
     * "Adicionar Equipamento ao Setor". Invoca o método `cod_equipamento_set()`
     * para realizar o cadastro de um equipamento associado a um setor no banco
     * de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnAdcSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdcSetActionPerformed

        cod_equipamento_set();
    }//GEN-LAST:event_btnAdcSetActionPerformed

    /**
     * Método `btnModEquiActionPerformed`. Manipula o evento de clique no botão
     * "Modificar Equipamento". Invoca o método `alterar_cod_equ()` para
     * atualizar as informações de um equipamento associado a um setor no banco
     * de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnModEquiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModEquiActionPerformed

        alterar_cod_equ();
    }//GEN-LAST:event_btnModEquiActionPerformed

    /**
     * Método `btnApaSetActionPerformed`. Manipula o evento de clique no botão
     * "Apagar Equipamento". Invoca o método `apqgar_cod_equ()` para remover um
     * equipamento específico do banco de dados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnApaSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApaSetActionPerformed

        apqgar_cod_equ();
    }//GEN-LAST:event_btnApaSetActionPerformed

    /**
     * Método `btnAltCodActionPerformed`. Manipula o evento de clique no botão
     * "Gerar Código para Equipamento". Invoca o método
     * `equipamento_setor_add()` para criar e exibir um código único para o
     * equipamento.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnAltCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltCodActionPerformed

        equipamento_setor_add();
    }//GEN-LAST:event_btnAltCodActionPerformed

    /**
     * Método `tblSetoresMouseClicked`. Manipula o evento de clique em uma linha
     * da tabela `tblSetores`. Invoca o método `setar_campos()` para atualizar
     * os campos e componentes da interface gráfica com base nos dados da linha
     * selecionada.
     *
     * @param evt Evento acionado pelo clique em uma linha da tabela.
     */
    private void tblSetoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSetoresMouseClicked

        setar_campos();
    }//GEN-LAST:event_tblSetoresMouseClicked

    /**
     * Método `btnNovoCadActionPerformed`. Manipula o evento de clique no botão
     * "Novo Cadastro". Redefine os campos e componentes da interface para
     * iniciar um novo cadastro de equipamento ou setor. Invoca métodos
     * auxiliares para ajustar e limpar a interface gráfica.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnNovoCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoCadActionPerformed

        txtEquSet.setText(null);
        txtIdEqu.setText(null);
        txtQuaSet.setText(null);
        txaNomEqui.setText("");
        txtCodEquSet.setText("");
        cboTipoEquiSet.setSelectedItem("Elétrica");
        cboTipoEquiSet.setEnabled(false);
        ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
        btnSetCreate.setEnabled(true);
        btnAltCod.setEnabled(false);
        btnApaSet.setEnabled(false);
        btnSetCreate.setEnabled(true);
        btnSetDelete.setEnabled(false);
        btnSetUpdate.setEnabled(false);
        btnModEqui.setEnabled(false);
        alinhar();
        limpar();
        pesquisar1();
    }//GEN-LAST:event_btnNovoCadActionPerformed

    /**
     * Método `txaNomEquiKeyReleased`. Manipula o evento de liberação de uma
     * tecla no campo de texto `txaNomEqui`. Controla o estado dos botões e
     * componentes baseando-se no texto inserido no campo `txaNomEqui` e no
     * identificador `txtIdEqu`.
     *
     * @param evt Evento acionado pela liberação de uma tecla.
     */
    private void txaNomEquiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaNomEquiKeyReleased

        String str;
        String ideq;
        str = txaNomEqui.getText();
        ideq = txtIdEqu.getText();
        if (str.equals("")) {
            btnAdcSet.setEnabled(false);
            cboTipoEquiSet.setEnabled(false);
        } else if (ideq.equals("")) {
            btnAdcSet.setEnabled(true);
            cboTipoEquiSet.setEnabled(true);
        }

    }//GEN-LAST:event_txaNomEquiKeyReleased

    /**
     * Método `formInternalFrameOpened`. Manipula o evento de abertura do
     * `InternalFrame`. Realiza as configurações iniciais da interface gráfica,
     * definindo comportamentos das tabelas, componentes e ajustando botões e
     * layouts.
     *
     * @param evt Evento acionado pela abertura do `InternalFrame`.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        tblSetores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCodEquiSet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lblSetor.setBackground(new Color(240, 240, 240, 65));
        lblBot.setBackground(new Color(240, 240, 240, 65));
        lblBot1.setBackground(new Color(240, 240, 240, 65));
        lblBot2.setBackground(new Color(240, 240, 240, 65));
        lblBot3.setBackground(new Color(240, 240, 240, 65));
        btnSetDelete.setEnabled(false);
        btnSetUpdate.setEnabled(false);
        btnAdcSet.setEnabled(false);
        btnApaSet.setEnabled(false);
        btnAltCod.setEnabled(false);
        btnModEqui.setEnabled(false);
        btnNovoCad.setEnabled(false);
        cboTipoEquiSet.setEnabled(false);

        txaNomEqui.setEditable(false);

        ((DefaultTableModel) tblCodEquiSet.getModel()).setRowCount(0);
        jScrollPane2.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        pesquisar1();
        alinhar();
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdcSet;
    private javax.swing.JButton btnAltCod;
    private javax.swing.JButton btnApaSet;
    private javax.swing.JButton btnModEqui;
    private javax.swing.JButton btnNovoCad;
    private javax.swing.JButton btnSetCreate;
    private javax.swing.JButton btnSetDelete;
    private javax.swing.JButton btnSetUpdate;
    private javax.swing.JComboBox<String> cboAndar;
    private javax.swing.JComboBox<String> cboTipoEquiSet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.prjmanutencao.telas.LabelPersonalizada lblBot;
    private com.prjmanutencao.telas.LabelPersonalizada lblBot1;
    private com.prjmanutencao.telas.LabelPersonalizada lblBot2;
    private com.prjmanutencao.telas.LabelPersonalizada lblBot3;
    private com.prjmanutencao.telas.LabelPersonalizada lblSetor;
    private javax.swing.JTable tblCodEquiSet;
    private javax.swing.JTable tblSetores;
    private javax.swing.JTextArea txaNomEqui;
    private javax.swing.JTextArea txaSetor;
    private javax.swing.JTextField txtCodEquSet;
    private javax.swing.JTextField txtEquSet;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdEqu;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtQuaSet;
    // End of variables declaration//GEN-END:variables
}
