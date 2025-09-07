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
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Método txtRetProdPesquiKeyReleased
 *
 * Disparado quando uma tecla é liberada no campo de texto txtRetProdPesqui.
 *
 * Este método chama tabelaProdInicioPesq(), que provavelmente atualiza a tabela
 * de produtos com base no texto digitado, permitindo uma busca dinâmica.
 *
 * Funcionalidade: - Detecta a liberação de uma tecla no campo de pesquisa. -
 * Atualiza a exibição da tabela de produtos conforme o texto digitado.
 *
 * Requisitos: - O campo txtRetProdPesqui deve estar vinculado a um KeyListener.
 * - O método tabelaProdInicioPesq() deve estar implementado corretamente.
 *
 * Observações: - Ideal para implementar busca em tempo real conforme o usuário
 * digita.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaRelPreventivas1 extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Construtor da classe TelaRelPreventivas1.
     *
     * Inicializa os componentes da interface gráfica e estabelece a conexão com
     * o banco de dados.
     *
     * Funcionalidade: - Chama o método initComponents() para configurar os
     * elementos visuais da tela. - Estabelece a conexão com o banco de dados
     * utilizando ModuloConexao.conector().
     *
     * Requisitos: - A classe ModuloConexao deve fornecer o método conector()
     * que retorna uma conexão válida. - O método initComponents() deve estar
     * corretamente implementado para montar a interface.
     *
     * Observações: - Este construtor é chamado automaticamente ao instanciar a
     * tela interna. - A conexão estabelecida pode ser usada pelos métodos da
     * classe para executar operações SQL.
     */
    public TelaRelPreventivas1() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    /**
     * Gera e exibe o relatório de manutenção preventiva com base na opção
     * selecionada no comboBox cboRelPreventivas. Cada opção corresponde a um
     * relatório Jasper (.jasper) diferente, relacionado a uma área específica
     * (Elétrica, Hidráulica, Civil ou Refrigeração) e a uma periodicidade
     * (Mensal, Trimestral ou Semestral).
     *
     * O método seleciona o arquivo de relatório apropriado e utiliza o
     * JasperReports para preenchê-lo e exibi-lo usando o JasperViewer. O ícone
     * da janela também é definido com base em um recurso de imagem.
     *
     * A conexão com o banco de dados é fechada após a exibição do relatório.
     *
     * Exceções tratadas: - JRException: se ocorrer erro ao preencher ou exibir
     * o relatório. - NumberFormatException: caso ocorra erro de formatação
     * numérica.
     */
    private void relatorioPreventiva() {
        String preve;
        preve = cboRelPreventivas.getSelectedItem().toString();

        InputStream caminhoRelativo = getClass().getResourceAsStream("/imprimir/RelatorioPreventiva.jasper");
        InputStream caminhoRelativo1 = getClass().getResourceAsStream("/imprimir/RelatorioEletrimestral.jasper");
        InputStream caminhoRelativo2 = getClass().getResourceAsStream("/imprimir/RelatorioEleSemestral.jasper");
        InputStream caminhoRelativo3 = getClass().getResourceAsStream("/imprimir/RelatorioHidMensal.jasper");
        InputStream caminhoRelativo4 = getClass().getResourceAsStream("/imprimir/RelatorioHidTrimestral.jasper");
        InputStream caminhoRelativo5 = getClass().getResourceAsStream("/imprimir/RelatorioHidSemestral.jasper");
        InputStream caminhoRelativo6 = getClass().getResourceAsStream("/imprimir/RelatorioCivMensal.jasper");
        InputStream caminhoRelativo7 = getClass().getResourceAsStream("/imprimir/RelatorioCivTrimestral.jasper");
        InputStream caminhoRelativo8 = getClass().getResourceAsStream("/imprimir/RelatorioCivSemestral.jasper");
        InputStream caminhoRelativ9 = getClass().getResourceAsStream("/imprimir/RelatorioRefMensal.jasper");
        InputStream caminhoRelativo10 = getClass().getResourceAsStream("/imprimir/RelatorioRefTrimestral.jasper");
        InputStream caminhoRelativo11 = getClass().getResourceAsStream("/imprimir/RelatorioRefSemestral.jasper");

        switch (preve) {
            case "Preventiva  Elétrica  Mensal":

                try {

//                    
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

//                
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
                break;

            case "Preventiva  Elétrica  Trimestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo1, null, conexao);
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
                break;

            case "Preventiva  Elétrica  Semestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo2, null, conexao);
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
                break;

            case "Preventiva  Hidráulica  Mensal":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo3, null, conexao);
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
                break;

            case "Preventiva  Hidráulica  Trimestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo4, null, conexao);
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
                break;

            case "Preventiva  Hidráulica  Semestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo5, null, conexao);
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
                break;

            case "Preventiva  Civil  Mensal":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo6, null, conexao);
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
                break;

            case "Preventiva  Civil  Trimestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo7, null, conexao);
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
                break;

            case "Preventiva  Civil  Semestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo8, null, conexao);
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
                break;

            case "Preventiva  Refrigeração  Mensal":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativ9, null, conexao);
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
                break;

            case "Preventiva  Refrigeração  Trimestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo10, null, conexao);
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
                break;

            case "Preventiva  Refrigeração  Semestral":

                try {
                    JasperPrint print;
                    JasperViewer viewer;
                    Image icone;

                    print = JasperFillManager.fillReport(caminhoRelativo11, null, conexao);
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
                break;

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

        jLabel1 = new javax.swing.JLabel();
        cboRelPreventivas = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Relatórios de Preventivas");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Relatórios de Preventiva");

        cboRelPreventivas.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cboRelPreventivas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Preventiva  Elétrica  Mensal", "Preventiva  Elétrica  Trimestral", "Preventiva  Elétrica  Semestral", "Preventiva  Hidráulica  Mensal", "Preventiva  Hidráulica  Trimestral", "Preventiva  Hidráulica  Semestral", "Preventiva  Civil  Mensal", "Preventiva  Civil  Trimestral", "Preventiva  Civil  Semestral", "Preventiva  Refrigeração  Mensal", "Preventiva  Refrigeração  Trimestral", "Preventiva  Refrigeração  Semestral" }));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 51));
        jButton1.setText("Imprimir");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboRelPreventivas, 0, 264, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboRelPreventivas, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        setBounds(0, 0, 300, 200);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Executado quando o botão de impressão é acionado. Exibe uma caixa de
     * diálogo de confirmação perguntando se o usuário deseja prosseguir com a
     * impressão do relatório preventivo.
     *
     * Se o usuário confirmar (clicar em "Sim"), o método relatorioPreventiva()
     * é chamado para gerar o relatório e a janela atual é fechada com
     * dispose().
     *
     * @param evt o evento de ação gerado pelo clique no botão
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a Impressão?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            relatorioPreventiva();
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboRelPreventivas;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
