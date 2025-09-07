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
import java.sql.*;
import static com.prjmanutencao.telas.TelaWebCam1.nome_foto;
import static com.prjmanutencao.telas.TelaWebCam1.tipo_prev;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Mostra a preventiva de Civil Mensal e a opções de anotação e fotos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaRefrigeracaoSem extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String situacao = null;
    String situacao1 = null;
    String situacao2 = null;
    String situacao3 = null;
    String situacao4 = null;
    String situacao5 = null;
    String situacao6 = null;
    String situacao7 = null;
    String situacao8 = null;
    String situacao9 = null;
    String situacao10 = null;
    String situacao11 = null;
    String situacao12 = null;
    String situacao13 = null;
    String situacao14 = null;
    String situacao15 = null;
    String situacao16 = null;
    String situacao17 = null;
    String situacao18 = null;
    String situacao19 = null;
    String situacao20 = null;
    String situacao21 = null;
    String situacao22 = null;
    String situacao23 = null;
    String situacao24 = null;
    String situacao25 = null;
    String situacao26 = null;
    String situacao27 = null;
    String situacao28 = null;
    String situacao29 = null;
    String situacao30 = null;
    String situacao31 = null;
    String situacao32 = null;
    String situacao33 = null;
    String situacao34 = null;
    String situacao35 = null;

    public static String nome1 = null;
    public static String id_prev11 = null;
    public static String foto_ele11 = null;

    public static String acesso = ("127.0.0.1");
    String log = "servidor";
    String sen = "12345";
    String dire = "refri_semestral";
    String data = null;

    /**
     * Construtor da classe `TelaRefrigeracaoSem`. Responsável por inicializar
     * os componentes da interface gráfica e configurar o estado inicial da
     * tela.
     *
     * Funcionalidades: - Chama `initComponents()` para montar os elementos
     * visuais da interface. - Estabelece a conexão com o banco de dados por
     * meio de `ModuloConexao.conector()`. - Oculta todos os campos de anotação
     * (`txpAAnot1` a `txpAAnot36`) ao iniciar a tela. - Oculta o painel de
     * imagem (`jPanel1`) por padrão. - Define o campo `txtIdPreEle` com o valor
     * da variável `id_prev11`, presumivelmente o identificador da preventiva
     * atual.
     *
     */
    public TelaRefrigeracaoSem() {
        initComponents();
        conexao = ModuloConexao.conector();
        txpAAnot1.setVisible(false);
        txpAAnot2.setVisible(false);
        txpAAnot3.setVisible(false);
        txpAAnot4.setVisible(false);
        txpAAnot5.setVisible(false);
        txpAAnot6.setVisible(false);
        txpAAnot7.setVisible(false);
        txpAAnot8.setVisible(false);
        txpAAnot9.setVisible(false);
        txpAAnot10.setVisible(false);
        txpAAnot11.setVisible(false);
        txpAAnot12.setVisible(false);
        txpAAnot13.setVisible(false);
        txpAAnot14.setVisible(false);
        txpAAnot15.setVisible(false);
        txpAAnot16.setVisible(false);
        txpAAnot17.setVisible(false);
        txpAAnot18.setVisible(false);
        txpAAnot19.setVisible(false);
        txpAAnot20.setVisible(false);
        txpAAnot21.setVisible(false);
        txpAAnot22.setVisible(false);
        txpAAnot23.setVisible(false);
        txpAAnot24.setVisible(false);
        txpAAnot25.setVisible(false);
        txpAAnot26.setVisible(false);
        txpAAnot27.setVisible(false);
        txpAAnot28.setVisible(false);
        txpAAnot29.setVisible(false);
        txpAAnot30.setVisible(false);
        txpAAnot31.setVisible(false);
        txpAAnot32.setVisible(false);
        txpAAnot33.setVisible(false);
        txpAAnot34.setVisible(false);
        txpAAnot35.setVisible(false);
        txpAAnot36.setVisible(false);

        jPanel1.setVisible(false);
        txtIdPreEle.setText(id_prev11);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     * Método `select_ref_sem`. Realiza a consulta de dados de uma preventiva de
     * refrigeração semestral no banco de dados com base no identificador
     * `id_prev11` e preenche os campos da interface com os valores retornados.
     *
     * Funcionalidades: - Executa um `SELECT` na tabela
     * `form_refrigeracao_semestral` utilizando `id_refrigeracao_semestral` como
     * filtro. - Se houver resultado: - Preenche os campos da interface gráfica
     * com os dados retornados: - `txtDatIni11`: data/hora de início da
     * preventiva - `txtDatFin`: data/hora de término - `txtTemPre`: tempo total
     * da preventiva - `txtDatHor`: tempo disponível - `txtNomTec`: nome do
     * técnico responsável - `txtSetPre`: setor da preventiva - `txtEquPr`: nome
     * do equipamento - `txtCodEquPre`: código do equipamento - `tipo_prev`:
     * tipo da preventiva (armazenado em variável)
     *
     * Observações: - O método trata exceções do tipo `SQLException` e exibe
     * mensagens de erro via `JOptionPane`. - Pode ser interessante validar se
     * `id_prev11` está preenchido antes de executar a consulta.
     */
    public void select_ref_sem() {

        String sql = "select id_refrigeracao_semestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, nome_prev, setor_prev, nome_equi_set, cod_equi_set, tipo_prev  from form_refrigeracao_semestral where id_refrigeracao_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_prev11);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDatIni11.setText(rs.getString(2));
                txtDatFin.setText(rs.getString(3));
                txtTemPre.setText(rs.getString(4));
                txtDatHor.setText(rs.getString(5));
                txtNomTec.setText(rs.getString(6));
                txtSetPre.setText(rs.getString(7));
                txtEquPr.setText(rs.getString(8));
                txtCodEquPre.setText(rs.getString(9));
                tipo_prev = (rs.getString(10));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `foto_prev`. Salva no banco de dados o registro de uma imagem
     * associada a uma preventiva de refrigeração semestral e, em caso de
     * sucesso, realiza o upload do arquivo correspondente via FTP.
     *
     * Funcionalidades: - Obtém o nome do arquivo da imagem a partir do campo
     * `txtFotPre11`. - Monta o caminho completo do arquivo com base na variável
     * `acesso` e o nome do arquivo. - Prepara e executa um comando `INSERT` na
     * tabela `arquivos_refrigeracao_semestral` com os seguintes dados: -
     * `nome_arq`: nome do arquivo (`txtFotPre11`) - `endere_arq`: caminho
     * completo (`acesso + prev`) - `id_refrigeracao_semestral`: identificador
     * da preventiva (`txtIdPreEle`) - Antes de executar o `INSERT`, valida se
     * os campos obrigatórios estão preenchidos. - Se a inserção for
     * bem-sucedida, chama o método `upload_file()` para enviar o arquivo ao
     * servidor FTP. - Exibe mensagens de sucesso ou erro ao usuário por meio de
     * `JOptionPane`.
     *
     * Observações: - A variável `prev` é redundante, pois já é obtida
     * diretamente de `txtFotPre11.getText()` nas chamadas subsequentes. -
     * Pode-se considerar mover a validação dos campos obrigatórios para antes
     * da preparação do `PreparedStatement`.
     */
    private void foto_prev() {
        String prev = null;
        prev = txtFotPre11.getText();
        String sql = "insert into arquivos_refrigeracao_semestral(nome_arq, endere_arq, id_refrigeracao_semestral) values (?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFotPre11.getText());
            pst.setString(2, acesso + prev);
            pst.setString(3, txtIdPreEle.getText());
            if ((txtFotPre11.getText().isEmpty()) || (txtIdPreEle.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int Adicionar = pst.executeUpdate();

                if (Adicionar > 0) {
                    upload_file();
                    JOptionPane.showMessageDialog(null, "Imagem Salva!");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `verificarArquivo`. Verifica se já existe um arquivo com o mesmo
     * nome no diretório FTP correspondente à preventiva atual.
     *
     * Funcionalidades: - Obtém o ID da preventiva (`idPrev`) e o nome do
     * arquivo (`nomeArq`) a partir dos campos da interface. - Conecta-se ao
     * servidor FTP utilizando as credenciais (`acesso`, `log`, `sen`). - Navega
     * até o diretório FTP correspondente à preventiva (`dire/idPrev`). - Lista
     * todos os arquivos presentes no diretório e compara com `nomeArq`. - Se
     * encontrar um nome igual, exibe uma mensagem informando que o arquivo já
     * existe.
     *
     * Observações: - O método utiliza `FTPClient` da biblioteca Apache Commons
     * Net. - A verificação é sensível a maiúsculas/minúsculas; se necessário,
     * pode-se usar `equalsIgnoreCase()`. - Pode ser interessante encerrar a
     * conexão FTP após a verificação com `ftp.logout()` e `ftp.disconnect()`. -
     * A linha `if (igual == true)` pode ser simplificada para `if (igual)`.
     */
    private void verificarArquivo() {

        String idPrev, nomeArq;
        idPrev = txtIdPreEle.getText();
        nomeArq = txtFotPre11.getText();
        boolean igual = false;
        try {

            FTPClient ftp = new FTPClient();
            //Fazendo a conexão
            ftp.connect(acesso);
            //Efetuando o Login
            ftp.login(log, sen);
            //Mundando o diretório de trabalho
            ftp.changeWorkingDirectory(dire + "/" + idPrev);
            //Adiquirindo o nome dos arquivos / diretórios existentes
            String[] arq = ftp.listNames();
            for (String f : arq) {
                if (f.equals(nomeArq)) {
                    igual = true;
                }
            }

            if (igual == true) {
                JOptionPane.showMessageDialog(null, "Já Existe um Arquivo com este Nome Salvo");

            }
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `upload_file`. Realiza o upload de um arquivo de imagem para um
     * servidor FTP, organizando-o em um diretório nomeado com base no ID da
     * preventiva.
     *
     * Funcionalidades: - Obtém o ID da preventiva (`idPrev`) e o nome do
     * arquivo (`dir`) a partir dos campos da interface. - Conecta-se ao
     * servidor FTP utilizando as credenciais (`acesso`, `log`, `sen`). - Navega
     * até o diretório base (`dire`) e cria um subdiretório com o nome `idPrev`,
     * se necessário. - Define o tipo de arquivo como binário para garantir a
     * integridade da imagem. - Envia o arquivo `foto_ele11` para o servidor com
     * o nome especificado em `dir`. - Em caso de sucesso, chama `limpar_foto()`
     * para limpar a interface; caso contrário, exibe uma mensagem de erro.
     *
     * Observações: - A linha `lblFotEltMen11.getIcon();` está isolada e não tem
     * efeito; pode ser removida. - O uso de `try-with-resources` garante o
     * fechamento automático do `FileInputStream`. - Pode ser interessante
     * validar se `foto_ele11` e `dir` não estão vazios antes de iniciar o
     * upload. - A chamada `arqEnviar.close();` é desnecessária dentro do bloco
     * `try-with-resources`.
     */
    private void upload_file() {
        String idPrev;
        idPrev = txtIdPreEle.getText();
        lblFotEltMen11.getIcon();

        String dir;
        dir = txtFotPre11.getText();
        FTPClient ftp = new FTPClient();
        try {

            ftp.connect(acesso);

            ftp.login(log, sen);

            ftp.makeDirectory(dire);
            ftp.changeWorkingDirectory(dire);
            ftp.makeDirectory(idPrev);
            ftp.changeWorkingDirectory(idPrev);
            ftp.enterLocalPassiveMode(); // Modo passivo

            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileInputStream arqEnviar = new FileInputStream(foto_ele11)) {
                if (ftp.storeFile(dir, arqEnviar)) {
                    limpar_foto();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Armazenar o Arquivo.");
                }
                arqEnviar.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método `prev_ini`. Inicia uma preventiva de refrigeração semestral,
     * atualizando seu status no banco de dados para "Aberta" e registrando a
     * data/hora de início.
     *
     * Funcionalidades: - Define o status da preventiva como "Aberta" e atualiza
     * o campo `txtStaPre` na interface. - Executa um comando `UPDATE` na tabela
     * `form_refrigeracao_semestral`, preenchendo os campos: - `tempo_ini_prev`:
     * data/hora de início da preventiva (`txtDatIni11`) - `situacao_prev`:
     * status "Aberta" - `id_refrigeracao_semestral`: identificador da
     * preventiva (`txtIdPreEle`) - Exibe uma mensagem de sucesso se a
     * atualização for realizada com sucesso, ou uma mensagem de erro caso
     * contrário.
     *
     * Observações: - O método trata exceções do tipo `SQLException` e
     * `HeadlessException`, exibindo mensagens amigáveis ao usuário.
     */
    public void prev_ini() {

        String status = "Aberta";
        txtStaPre.setText(status);

        String sql = "update form_refrigeracao_semestral set tempo_ini_prev = ?,  situacao_prev = ?  where id_refrigeracao_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDatIni11.getText());
            pst.setString(2, status);
            pst.setString(3, txtIdPreEle.getText());
            int alterar1 = pst.executeUpdate();
            if (alterar1 > 0) {
                JOptionPane.showMessageDialog(null, "Preventiva Iniciada!");

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Iniciar Preventiva");
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `iniciar_prev`. Finaliza ou atualiza uma preventiva de
     * refrigeração semestral, salvando no banco de dados os dados de
     * encerramento, tempo total e os resultados de cada item avaliado (A1 a
     * A36), incluindo observações.
     *
     * Funcionalidades: - Obtém o status atual da preventiva a partir do campo
     * `txtStaPre`. - Executa um comando `UPDATE` na tabela
     * `form_refrigeracao_semestral`, preenchendo os campos: - `tempo_fin_prev`:
     * data/hora de término da preventiva (`txtDatFin`) - `tempo_total_prev`:
     * tempo total calculado (`txtTemPre`) - `situacao_prev`: status atual da
     * preventiva - `a1` a `a36`: situação de cada item (ex: "Conforme", "Não
     * Conforme") - `obser1` a `obser36`: observações associadas a cada item
     * (`txtaA1` a `txtaA36`) - Identifica a preventiva pelo campo
     * `id_refrigeracao_semestral` (`txtIdPreEle`) - Exibe mensagens de sucesso
     * ou erro com base no resultado da operação: - Se status for "Encerrada",
     * exibe mensagem de encerramento e fecha a janela. - Caso contrário, apenas
     * confirma o salvamento dos dados.
     *
     * Observações: - O método trata exceções do tipo `SQLException` e exibe
     * mensagens amigáveis ao usuário. - A conexão com o banco é encerrada com
     * `conexao.close()` após a operação, independentemente do resultado.
     */
    private void iniciar_prev() {
        String status = txtStaPre.getText();

        String sql = "update form_refrigeracao_semestral set tempo_fin_prev = ?, tempo_total_prev = ?, situacao_prev = ?, a1 = ?, obser1 = ?, a2 = ?, obser2 = ?, a3 = ?, obser3 = ?, a4 = ?, obser4 = ?, a5 = ?, obser5 = ?, a6 = ?, obser6 = ?, a7 = ?, obser7 = ?, a8 = ?, obser8 = ?, a9 = ?, obser9 = ?, a10 = ?, obser10 = ?, a11 = ?, obser11 = ?, a12 = ?, obser12 = ?, a13 = ?, obser13 = ?, a14 = ?, obser14 = ?, a15 = ?, obser15 = ?, a16 = ?, obser16 = ?, a17 = ?, obser17 = ?, a18 = ?, obser18 = ?, a19 = ?, obser19 = ?, a20 = ?, obser20 = ?, a21 = ?, obser21 = ?, a22 = ?, obser22 = ?,a23 = ?, obser23 = ?, a24 = ?, obser24 = ?, a25 = ?, obser25 = ?, a26 = ?, obser26 = ?, a27 = ?, obser27 = ?, a28 = ?, obser28 = ?, a29 = ?, obser29 = ?, a30 = ?, obser30 = ?, a31 = ?, obser31 = ?, a32 = ?, obser32 = ?, a33 = ?, obser33 = ?, a34 = ?, obser34 = ?, a35 = ?, obser35 = ?, a36 = ?, obser36 = ? where id_refrigeracao_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDatFin.getText());
            pst.setString(2, txtTemPre.getText());
            pst.setString(3, txtStaPre.getText());

            pst.setString(4, situacao);
            pst.setString(5, txtaA1.getText());

            pst.setString(6, situacao1);
            pst.setString(7, txtaA2.getText());

            pst.setString(8, situacao2);
            pst.setString(9, txtaA3.getText());

            pst.setString(10, situacao3);
            pst.setString(11, txtaA4.getText());

            pst.setString(12, situacao4);
            pst.setString(13, txtaA5.getText());

            pst.setString(14, situacao5);
            pst.setString(15, txtaA6.getText());

            pst.setString(16, situacao6);
            pst.setString(17, txtaA7.getText());

            pst.setString(18, situacao7);
            pst.setString(19, txtaA8.getText());

            pst.setString(20, situacao8);
            pst.setString(21, txtaA9.getText());

            pst.setString(22, situacao9);
            pst.setString(23, txtaA10.getText());

            pst.setString(24, situacao10);
            pst.setString(25, txtaA11.getText());

            pst.setString(26, situacao11);
            pst.setString(27, txtaA12.getText());

            pst.setString(28, situacao12);
            pst.setString(29, txtaA13.getText());

            pst.setString(30, situacao13);
            pst.setString(31, txtaA14.getText());

            pst.setString(32, situacao14);
            pst.setString(33, txtaA15.getText());

            pst.setString(34, situacao15);
            pst.setString(35, txtaA16.getText());

            pst.setString(36, situacao16);
            pst.setString(37, txtaA17.getText());

            pst.setString(38, situacao17);
            pst.setString(39, txtaA18.getText());

            pst.setString(40, situacao18);
            pst.setString(41, txtaA19.getText());

            pst.setString(42, situacao19);
            pst.setString(43, txtaA20.getText());

            pst.setString(44, situacao20);
            pst.setString(45, txtaA21.getText());

            pst.setString(46, situacao21);
            pst.setString(47, txtaA22.getText());

            pst.setString(48, situacao22);
            pst.setString(49, txtaA23.getText());

            pst.setString(50, situacao23);
            pst.setString(51, txtaA24.getText());

            pst.setString(52, situacao24);
            pst.setString(53, txtaA25.getText());

            pst.setString(54, situacao25);
            pst.setString(55, txtaA26.getText());

            pst.setString(56, situacao26);
            pst.setString(57, txtaA27.getText());

            pst.setString(58, situacao27);
            pst.setString(59, txtaA28.getText());

            pst.setString(60, situacao28);
            pst.setString(61, txtaA29.getText());

            pst.setString(62, situacao29);
            pst.setString(63, txtaA30.getText());

            pst.setString(64, situacao30);
            pst.setString(65, txtaA31.getText());

            pst.setString(66, situacao31);
            pst.setString(67, txtaA32.getText());

            pst.setString(68, situacao32);
            pst.setString(69, txtaA33.getText());

            pst.setString(70, situacao33);
            pst.setString(71, txtaA34.getText());

            pst.setString(72, situacao34);
            pst.setString(73, txtaA35.getText());

            pst.setString(74, situacao35);
            pst.setString(75, txtaA36.getText());

            pst.setString(76, txtIdPreEle.getText());
            int alterar1 = pst.executeUpdate();
            if (alterar1 > 0) {

                if (status.equals("Encerrada")) {
                    JOptionPane.showMessageDialog(null, "Preventiva Encerrada com Sucesso!");
                    conexao.close();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Dados Salvos!");
                    conexao.close();
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Alterar os Dados!");
                conexao.close();
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `select_arq`. Abre um seletor de arquivos para o usuário escolher
     * uma imagem JPEG e prepara o arquivo para visualização e upload.
     *
     * Funcionalidades: - Limpa o ícone atual do rótulo `lblFotEltMen11` e
     * desativa o botão de salvar (`btnSalFotPre11`). - Gera um número aleatório
     * entre 1 e 300000 para compor um nome único de imagem. - Abre um
     * `JFileChooser` com filtro para arquivos `.jpg`, apontando inicialmente
     * para a área de trabalho. - Após a seleção: - Armazena o caminho do
     * arquivo em `foto_ele11`. - Define o nome do arquivo em `txtFotPre11` com
     * base no prefixo `nome_foto` e o número aleatório. - Chama
     * `verificarArquivo()` para checar duplicidade ou existência no servidor. -
     * Redimensiona e exibe a imagem no rótulo `lblFotEltMen11`. - Ativa o botão
     * de salvar imagem.
     *
     * Observações: - O método trata exceções do tipo `HeadlessException` e
     * exibe mensagens de erro via `JOptionPane`. - **Recomendação**: adicionar
     * uma verificação para garantir que `file.getSelectedFile()` não seja
     * `null` antes de acessar `getPath()`, evitando `NullPointerException` caso
     * o usuário cancele a seleção.
     */
    private void select_arq() {

        lblFotEltMen11.setIcon(null);
        btnSalFotPre11.setEnabled(false);

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {

            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagem em JPEG ", "jpg");
            file.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            file.setFileFilter(filtro);
            file.setDialogTitle("Carregar Foto");
            file.showOpenDialog(this);
            foto_ele11 = file.getSelectedFile().getPath();
            txtFotPre11.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));
            verificarArquivo();
            ImageIcon icon = new ImageIcon(foto_ele11);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen11.getWidth(), lblFotEltMen11.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen11.setIcon(icon);
            btnSalFotPre11.setEnabled(true);

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método `cauc_tempo`. Calcula a diferença entre a data/hora de início e
     * término de uma preventiva e exibe o tempo total decorrido no formato "dd
     * - HH : mm : ss" no campo `txtTemPre`.
     *
     * Funcionalidades: - Obtém as strings de data/hora dos campos `txtDatIni11`
     * e `txtDatFin`. - Converte as strings para objetos `Date` usando o formato
     * "dd/MM/yyyy HH:mm:ss". - Calcula a diferença entre as datas em
     * milissegundos. - Converte a diferença em dias, horas, minutos e segundos.
     * - Formata o tempo com zeros à esquerda para manter a consistência visual.
     * - Exibe o resultado formatado no campo `txtTemPre`.
     *
     * Observações: - O método trata `ParseException` e exibe uma mensagem de
     * erro caso a conversão de datas falhe.
     */
    private void cauc_tempo() {

        String dateStart = txtDatIni11.getText();
        String dateStop = txtDatFin.getText();

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        java.util.Date d1 = null;
        java.util.Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            txtTemPre.setText(((diffDays <= 9 ? "0" : "") + diffDays) + (" - ") + ((diffHours <= 9 ? "0" : "") + diffHours) + (" : ") + ((diffMinutes <= 9 ? "0" : "") + diffMinutes) + (" : ") + ((diffSeconds <= 9 ? "0" : "") + diffSeconds));

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Ocorreu um erro ao calcular as Horas Trabalhadas!", "Atenção", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Método limpar_foto Responsável por limpar e ocultar os elementos
     * relacionados à visualização de imagem.
     *
     * Funcionalidades executadas: - Oculta o painel jPanel1 que exibe a imagem.
     * - Remove o ícone da label lblFotEltMen11. - Limpa o texto do campo
     * txtFotPre11. - Desabilita o botão btnSalFotPre11 para impedir salvamento
     * de imagem inexistente.
     */
    private void limpar_foto() {
        jPanel1.setVisible(false);
        lblFotEltMen11.setIcon(null);
        txtFotPre11.setText(null);
        btnSalFotPre11.setEnabled(false);
    }

    /**
     * Método `select_ref_sem1`. Recupera do banco de dados os dados completos
     * de uma preventiva de refrigeração semestral, preenchendo os campos da
     * interface gráfica com as informações armazenadas.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_refrigeracao_semestral` com base no ID `id_prev11`. - Preenche os
     * campos da interface com os dados retornados, incluindo: - Datas e
     * horários (`txtDatHor`, `txtDatFin`, `txtTemPre`, `txtDatIni11`) -
     * Informações gerais (`txtStaPre`, `txtNomTec`, `txtSetPre`, `txtEquPr`,
     * `txtCodEquPre`, `tipo_prev`) - Situações e observações dos itens A1 a
     * A36, com seleção automática dos radio buttons correspondentes e
     * preenchimento dos campos de texto (`txtaA1` a `txtaA36`).
     *
     * Observações: - A seleção dos radio buttons é feita com base no valor
     * retornado ("Conforme" ou "Não Conforme"). - O método trata exceções do
     * tipo `SQLException` e exibe mensagens de erro via `JOptionPane`. - A
     * lógica de preenchimento dos itens está parcialmente implementada (até A18
     * e A36), e pode ser completada com os demais itens (A19 a A34) para
     * garantir consistência.
     */
    public void select_ref_sem1() {

        String sql = "select id_refrigeracao_semestral, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, tipo_prev, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27,   a28, obser28,  a29, obser29,   a30, obser30,  a31, obser31,  a32, obser32, a33, obser33, a34, obser34, a35, obser35, a36, obser36 from form_refrigeracao_semestral where id_refrigeracao_semestral = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_prev11);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDatHor.setText(rs.getString(2));
                txtDatFin.setText(rs.getString(3));
                txtTemPre.setText(rs.getString(4));
                txtDatIni11.setText(rs.getString(5));
                txtStaPre.setText(rs.getString(6));
                txtNomTec.setText(rs.getString(7));
                txtSetPre.setText(rs.getString(8));
                txtEquPr.setText(rs.getString(9));
                txtCodEquPre.setText(rs.getString(10));
                tipo_prev = (rs.getString(11));

                String situ1 = (rs.getString(12));
                if (situ1.equals("Não Conforme")) {
                    rbtA1.setSelected(true);
                    situacao = "Não Conforme";
                } else {
                    rbtA11.setSelected(true);
                    situacao = "Conforme";
                }
                txtaA1.setText(rs.getString(13));

                String situ2 = (rs.getString(14));
                if (situ2.equals("Não Conforme")) {
                    rbtA2.setSelected(true);
                    situacao1 = "Não Conforme";
                } else {
                    rbtA12.setSelected(true);
                    situacao1 = "Conforme";
                };
                txtaA2.setText(rs.getString(15));
//
                String situ3 = (rs.getString(16));
                if (situ3.equals("Não Conforme")) {
                    rbtA3.setSelected(true);
                    situacao2 = "Não Conforme";
                } else {
                    rbtA13.setSelected(true);
                    situacao2 = "Conforme";
                }
                txtaA3.setText(rs.getString(17));
//
                String situ4 = (rs.getString(18));
                if (situ4.equals("Não Conforme")) {
                    rbtA4.setSelected(true);
                    situacao3 = "Não Conforme";
                } else {
                    rbtA14.setSelected(true);
                    situacao3 = "Conforme";
                }
                txtaA4.setText(rs.getString(19));
//
                String situ5 = (rs.getString(20));
                if (situ5.equals("Não Conforme")) {
                    rbtA5.setSelected(true);
                    situacao4 = "Não Conforme";
                } else {
                    rbtA15.setSelected(true);
                    situacao4 = "Conforme";
                }
                txtaA5.setText(rs.getString(21));
//
                String situ6 = (rs.getString(22));
                if (situ6.equals("Não Conforme")) {
                    rbtA6.setSelected(true);
                    situacao5 = "Não Conforme";
                } else {
                    rbtA16.setSelected(true);
                    situacao5 = "Conforme";
                }
                txtaA6.setText(rs.getString(23));
//
                String situ7 = (rs.getString(24));
                if (situ7.equals("Não Conforme")) {
                    rbtA7.setSelected(true);
                    situacao6 = "Não Conforme";
                } else {
                    rbtA17.setSelected(true);
                    situacao6 = "Conforme";
                }
                txtaA7.setText(rs.getString(25));
//
                String situ8 = (rs.getString(26));
                if (situ8.equals("Não Conforme")) {
                    rbtA8.setSelected(true);
                    situacao7 = "Não Conforme";
                } else {
                    rbtA18.setSelected(true);
                    situacao7 = "Conforme";
                }
                txtaA8.setText(rs.getString(27));
//
                String situ9 = (rs.getString(28));
                if (situ9.equals("Não Conforme")) {
                    rbtA9.setSelected(true);
                    situacao8 = "Não Conforme";
                } else {
                    rbtA19.setSelected(true);
                    situacao8 = "Conforme";
                }
                txtaA9.setText(rs.getString(29));
//
                String situ10 = (rs.getString(30));
                if (situ10.equals("Não Conforme")) {
                    rbtA10.setSelected(true);
                    situacao9 = "Não Conforme";
                } else {
                    rbtA20.setSelected(true);
                    situacao9 = "Conforme";
                }
                txtaA10.setText(rs.getString(31));
//
                String situ11 = (rs.getString(32));
                if (situ11.equals("Não Conforme")) {
                    rbtA21.setSelected(true);
                    situacao10 = "Não Conforme";
                } else {
                    rbtA31.setSelected(true);
                    situacao10 = "Conforme";
                }
                txtaA11.setText(rs.getString(33));
//
                String situ12 = (rs.getString(34));
                if (situ12.equals("Não Conforme")) {
                    rbtA22.setSelected(true);
                    situacao11 = "Não Conforme";
                } else {
                    rbtA32.setSelected(true);
                    situacao11 = "Conforme";
                }
                txtaA12.setText(rs.getString(35));
//
                String situ13 = (rs.getString(36));
                if (situ13.equals("Não Conforme")) {
                    rbtA23.setSelected(true);
                    situacao12 = "Não Conforme";
                } else {
                    rbtA33.setSelected(true);
                    situacao12 = "Conforme";
                }
                txtaA13.setText(rs.getString(37));
//
                String situ14 = (rs.getString(38));
                if (situ14.equals("Não Conforme")) {
                    rbtA24.setSelected(true);
                    situacao13 = "Não Conforme";
                } else {
                    rbtA34.setSelected(true);
                    situacao13 = "Conforme";
                }
                txtaA14.setText(rs.getString(39));
//
                String situ15 = (rs.getString(40));
                if (situ15.equals("Não Conforme")) {
                    rbtA25.setSelected(true);
                    situacao14 = "Não Conforme";
                } else {
                    rbtA35.setSelected(true);
                    situacao14 = "Conforme";
                }
                txtaA15.setText(rs.getString(41));
//
                String situ16 = (rs.getString(42));
                if (situ16.equals("Não Conforme")) {
                    rbtA26.setSelected(true);
                    situacao15 = "Não Conforme";
                } else {
                    rbtA36.setSelected(true);
                    situacao15 = "Conforme";
                }
                txtaA16.setText(rs.getString(43));
//
                String situ17 = (rs.getString(44));
                if (situ17.equals("Não Conforme")) {
                    rbtA27.setSelected(true);
                    situacao16 = "Não Conforme";
                } else {
                    rbtA37.setSelected(true);
                    situacao16 = "Conforme";
                }
                txtaA17.setText(rs.getString(45));
//
                String situ18 = (rs.getString(46));
                if (situ18.equals("Não Conforme")) {
                    rbtA28.setSelected(true);
                    situacao17 = "Não Conforme";
                } else {
                    rbtA38.setSelected(true);
                    situacao17 = "Conforme";
                }
                txtaA18.setText(rs.getString(47));
//
                String situ19 = (rs.getString(48));
                if (situ19.equals("Não Conforme")) {
                    rbtA29.setSelected(true);
                    situacao18 = "Não Conforme";
                } else {
                    rbtA39.setSelected(true);
                    situacao18 = "Conforme";
                }
                txtaA19.setText(rs.getString(49));
//
                String situ20 = (rs.getString(50));
                if (situ20.equals("Não Conforme")) {
                    rbtA30.setSelected(true);
                    situacao19 = "Não Conforme";
                } else {
                    rbtA40.setSelected(true);
                    situacao19 = "Conforme";
                }
                txtaA20.setText(rs.getString(51));
//
                String situ21 = (rs.getString(52));
                if (situ21.equals("Não Conforme")) {
                    rbtA41.setSelected(true);
                    situacao20 = "Não Conforme";
                } else {
                    rbtA51.setSelected(true);
                    situacao20 = "Conforme";
                }
                txtaA21.setText(rs.getString(53));
//
                String situ22 = (rs.getString(54));
                if (situ22.equals("Não Conforme")) {
                    rbtA42.setSelected(true);
                    situacao21 = "Não Conforme";
                } else {
                    rbtA52.setSelected(true);
                    situacao21 = "Conforme";
                }
                txtaA22.setText(rs.getString(55));
//
                String situ23 = (rs.getString(56));
                if (situ23.equals("Não Conforme")) {
                    rbtA43.setSelected(true);
                    situacao22 = "Não Conforme";
                } else {
                    rbtA53.setSelected(true);
                    situacao22 = "Conforme";
                }
                txtaA23.setText(rs.getString(57));
//
                String situ24 = (rs.getString(58));
                if (situ24.equals("Não Conforme")) {
                    rbtA44.setSelected(true);
                    situacao23 = "Não Conforme";
                } else {
                    rbtA54.setSelected(true);
                    situacao23 = "Conforme";
                }
                txtaA24.setText(rs.getString(59));
//
                String situ25 = (rs.getString(60));
                if (situ25.equals("Não Conforme")) {
                    rbtA45.setSelected(true);
                    situacao24 = "Não Conforme";
                } else {
                    rbtA55.setSelected(true);
                    situacao24 = "Conforme";
                }
                txtaA25.setText(rs.getString(61));
//
                String situ26 = (rs.getString(62));
                if (situ26.equals("Não Conforme")) {
                    rbtA46.setSelected(true);
                    situacao25 = "Não Conforme";
                } else {
                    rbtA56.setSelected(true);
                    situacao25 = "Conforme";
                }
                txtaA26.setText(rs.getString(63));
//
                String situ27 = (rs.getString(64));
                if (situ27.equals("Não Conforme")) {
                    rbtA47.setSelected(true);
                    situacao26 = "Não Conforme";
                } else {
                    rbtA57.setSelected(true);
                    situacao26 = "Conforme";
                }
                txtaA27.setText(rs.getString(65));
//
                String situ28 = (rs.getString(66));
                if (situ28.equals("Não Conforme")) {
                    rbtA48.setSelected(true);
                    situacao27 = "Não Conforme";
                } else {
                    rbtA58.setSelected(true);
                    situacao27 = "Conforme";
                }
                txtaA28.setText(rs.getString(67));
//
                String situ29 = (rs.getString(68));
                if (situ29.equals("Não Conforme")) {
                    rbtA49.setSelected(true);
                    situacao28 = "Não Conforme";
                } else {
                    rbtA59.setSelected(true);
                    situacao28 = "Conforme";
                }
                txtaA29.setText(rs.getString(69));
//
                String situ30 = (rs.getString(70));
                if (situ30.equals("Não Conforme")) {
                    rbtA50.setSelected(true);
                    situacao29 = "Não Conforme";
                } else {
                    rbtA60.setSelected(true);
                    situacao29 = "Conforme";
                }
                txtaA30.setText(rs.getString(71));
//
                String situ31 = (rs.getString(72));
                if (situ31.equals("Não Conforme")) {
                    rbtA61.setSelected(true);
                    situacao30 = "Não Conforme";
                } else {
                    rbtA71.setSelected(true);
                    situacao30 = "Conforme";
                }
                txtaA31.setText(rs.getString(73));
//
                String situ32 = (rs.getString(74));
                if (situ32.equals("Não Conforme")) {
                    rbtA62.setSelected(true);
                    situacao31 = "Não Conforme";
                } else {
                    rbtA72.setSelected(true);
                    situacao31 = "Conforme";
                }
                txtaA32.setText(rs.getString(75));

                String situ33 = (rs.getString(76));
                if (situ33.equals("Não Conforme")) {
                    rbtA63.setSelected(true);
                    situacao32 = "Não Conforme";
                } else {
                    rbtA73.setSelected(true);
                    situacao32 = "Conforme";
                }
                txtaA33.setText(rs.getString(77));

                String situ34 = (rs.getString(78));
                if (situ34.equals("Não Conforme")) {
                    rbtA64.setSelected(true);
                    situacao33 = "Não Conforme";
                } else {
                    rbtA74.setSelected(true);
                    situacao33 = "Conforme";
                };
                txtaA34.setText(rs.getString(79));
//
                String situ35 = (rs.getString(80));
                if (situ35.equals("Não Conforme")) {
                    rbtA65.setSelected(true);
                    situacao34 = "Não Conforme";
                } else {
                    rbtA75.setSelected(true);
                    situacao34 = "Conforme";
                }
                txtaA35.setText(rs.getString(81));
//
                String situ36 = (rs.getString(82));
                if (situ36.equals("Não Conforme")) {
                    rbtA66.setSelected(true);
                    situacao35 = "Não Conforme";
                } else {
                    rbtA76.setSelected(true);
                    situacao35 = "Conforme";
                }
                txtaA36.setText(rs.getString(83));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * KeyAdapter validadorTeclado
     *
     * Adaptador de teclado personalizado para validar a entrada de texto em um
     * JTextArea.
     *
     * Funcionalidades: - Permite apenas caracteres alfanuméricos, vírgula,
     * parênteses e espaço. - Limita o texto a no máximo 200 caracteres. - Caso
     * a entrada viole essas regras, o evento é consumido (bloqueado). -
     * Opcionalmente, pode emitir um som ao bloquear a entrada (linha
     * comentada).
     *
     * Uso: - Adicione este `KeyAdapter` ao componente JTextArea com
     * `addKeyListener(validadorTeclado)`.
     */
    KeyAdapter validadorTeclado = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent evt) {
            JTextArea campo = (JTextArea) evt.getSource();
            String textoAtual = campo.getText() + evt.getKeyChar();

            // Validação personalizada: caracteres permitidos e limite de 200
            if (textoAtual.length() > 200 || !String.valueOf(evt.getKeyChar()).matches("[a-zA-Z0-9.,()áÁàãÃÀéÉèÈêÊôÔõÕóÓòíÍìÌÒÇç ]")) {
                evt.consume();
//                Toolkit.getDefaultToolkit().beep(); // opcional: som ao bloquear
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grbA1 = new javax.swing.ButtonGroup();
        grbA2 = new javax.swing.ButtonGroup();
        grbA3 = new javax.swing.ButtonGroup();
        grbA4 = new javax.swing.ButtonGroup();
        grbA5 = new javax.swing.ButtonGroup();
        grbA6 = new javax.swing.ButtonGroup();
        grbA7 = new javax.swing.ButtonGroup();
        grbA8 = new javax.swing.ButtonGroup();
        grbA9 = new javax.swing.ButtonGroup();
        grbA10 = new javax.swing.ButtonGroup();
        grbA11 = new javax.swing.ButtonGroup();
        grbA12 = new javax.swing.ButtonGroup();
        grbA13 = new javax.swing.ButtonGroup();
        grbA14 = new javax.swing.ButtonGroup();
        grbA15 = new javax.swing.ButtonGroup();
        grbA16 = new javax.swing.ButtonGroup();
        grbA17 = new javax.swing.ButtonGroup();
        grbA18 = new javax.swing.ButtonGroup();
        grbA19 = new javax.swing.ButtonGroup();
        gbrA20 = new javax.swing.ButtonGroup();
        gbrA21 = new javax.swing.ButtonGroup();
        gbrA22 = new javax.swing.ButtonGroup();
        gbrA23 = new javax.swing.ButtonGroup();
        gbrA24 = new javax.swing.ButtonGroup();
        gbrA25 = new javax.swing.ButtonGroup();
        gbrA26 = new javax.swing.ButtonGroup();
        gbrA27 = new javax.swing.ButtonGroup();
        gbrA28 = new javax.swing.ButtonGroup();
        gbrA29 = new javax.swing.ButtonGroup();
        gbrA30 = new javax.swing.ButtonGroup();
        gbrA31 = new javax.swing.ButtonGroup();
        gbrA32 = new javax.swing.ButtonGroup();
        grbA33 = new javax.swing.ButtonGroup();
        grbA34 = new javax.swing.ButtonGroup();
        grbA35 = new javax.swing.ButtonGroup();
        grbA36 = new javax.swing.ButtonGroup();
        grbA37 = new javax.swing.ButtonGroup();
        grbA38 = new javax.swing.ButtonGroup();
        grbA39 = new javax.swing.ButtonGroup();
        grbA40 = new javax.swing.ButtonGroup();
        grbA41 = new javax.swing.ButtonGroup();
        grbA42 = new javax.swing.ButtonGroup();
        grbA43 = new javax.swing.ButtonGroup();
        grbA44 = new javax.swing.ButtonGroup();
        grbA45 = new javax.swing.ButtonGroup();
        grbA46 = new javax.swing.ButtonGroup();
        grbA47 = new javax.swing.ButtonGroup();
        grbA48 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblFotEltMen11 = new javax.swing.JLabel();
        txtFotPre11 = new javax.swing.JTextField();
        btnSalFotPre11 = new javax.swing.JButton();
        btnArqFotoPre = new javax.swing.JButton();
        txtCanFot = new javax.swing.JButton();
        btnCamPre = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        rbtA1 = new javax.swing.JRadioButton();
        rbtA11 = new javax.swing.JRadioButton();
        btnAnoA1 = new javax.swing.JToggleButton();
        btnFotoA1 = new javax.swing.JButton();
        txpAAnot1 = new javax.swing.JScrollPane();
        txtaA1 = new javax.swing.JTextArea();
        txtIdPreEle = new javax.swing.JTextField();
        txtDatIni11 = new javax.swing.JTextField();
        txtDatFin = new javax.swing.JTextField();
        txtTemPre = new javax.swing.JTextField();
        txtDatHor = new javax.swing.JTextField();
        txtNomTec = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSetPre = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtEquPr = new javax.swing.JTextArea();
        txtCodEquPre = new javax.swing.JTextField();
        txtStaPre = new javax.swing.JTextField();
        rbtA12 = new javax.swing.JRadioButton();
        rbtA2 = new javax.swing.JRadioButton();
        btnFotoA2 = new javax.swing.JButton();
        btnAnoA2 = new javax.swing.JToggleButton();
        txpAAnot2 = new javax.swing.JScrollPane();
        txtaA2 = new javax.swing.JTextArea();
        rbtA3 = new javax.swing.JRadioButton();
        rbtA13 = new javax.swing.JRadioButton();
        txpAAnot3 = new javax.swing.JScrollPane();
        txtaA3 = new javax.swing.JTextArea();
        btnAnoA3 = new javax.swing.JToggleButton();
        btnFotoA3 = new javax.swing.JButton();
        rbtA4 = new javax.swing.JRadioButton();
        txpAAnot4 = new javax.swing.JScrollPane();
        txtaA4 = new javax.swing.JTextArea();
        rbtA14 = new javax.swing.JRadioButton();
        btnAnoA4 = new javax.swing.JToggleButton();
        btnFotoA4 = new javax.swing.JButton();
        rbtA5 = new javax.swing.JRadioButton();
        txpAAnot5 = new javax.swing.JScrollPane();
        txtaA5 = new javax.swing.JTextArea();
        rbtA15 = new javax.swing.JRadioButton();
        btnAnoA5 = new javax.swing.JToggleButton();
        btnFotoA5 = new javax.swing.JButton();
        rbtA6 = new javax.swing.JRadioButton();
        txpAAnot6 = new javax.swing.JScrollPane();
        txtaA6 = new javax.swing.JTextArea();
        rbtA16 = new javax.swing.JRadioButton();
        btnAnoA6 = new javax.swing.JToggleButton();
        btnFotoA6 = new javax.swing.JButton();
        rbtA7 = new javax.swing.JRadioButton();
        txpAAnot7 = new javax.swing.JScrollPane();
        txtaA7 = new javax.swing.JTextArea();
        rbtA17 = new javax.swing.JRadioButton();
        btnAnoA7 = new javax.swing.JToggleButton();
        btnFotoA7 = new javax.swing.JButton();
        rbtA8 = new javax.swing.JRadioButton();
        txpAAnot8 = new javax.swing.JScrollPane();
        txtaA8 = new javax.swing.JTextArea();
        rbtA18 = new javax.swing.JRadioButton();
        btnAnoA8 = new javax.swing.JToggleButton();
        btnFotoA8 = new javax.swing.JButton();
        rbtA9 = new javax.swing.JRadioButton();
        txpAAnot9 = new javax.swing.JScrollPane();
        txtaA9 = new javax.swing.JTextArea();
        rbtA19 = new javax.swing.JRadioButton();
        btnAnoA9 = new javax.swing.JToggleButton();
        btnFotoA9 = new javax.swing.JButton();
        rbtA10 = new javax.swing.JRadioButton();
        txpAAnot10 = new javax.swing.JScrollPane();
        txtaA10 = new javax.swing.JTextArea();
        rbtA20 = new javax.swing.JRadioButton();
        btnAnoA10 = new javax.swing.JToggleButton();
        btnFotoA10 = new javax.swing.JButton();
        rbtA21 = new javax.swing.JRadioButton();
        rbtA31 = new javax.swing.JRadioButton();
        txpAAnot11 = new javax.swing.JScrollPane();
        txtaA11 = new javax.swing.JTextArea();
        btnAnoA11 = new javax.swing.JToggleButton();
        btnFotoA11 = new javax.swing.JButton();
        rbtA22 = new javax.swing.JRadioButton();
        txpAAnot12 = new javax.swing.JScrollPane();
        txtaA12 = new javax.swing.JTextArea();
        rbtA32 = new javax.swing.JRadioButton();
        btnAnoA12 = new javax.swing.JToggleButton();
        btnFotoA12 = new javax.swing.JButton();
        rbtA23 = new javax.swing.JRadioButton();
        txpAAnot13 = new javax.swing.JScrollPane();
        txtaA13 = new javax.swing.JTextArea();
        rbtA33 = new javax.swing.JRadioButton();
        btnAnoA13 = new javax.swing.JToggleButton();
        btnFotoA13 = new javax.swing.JButton();
        rbtA24 = new javax.swing.JRadioButton();
        txpAAnot14 = new javax.swing.JScrollPane();
        txtaA14 = new javax.swing.JTextArea();
        rbtA34 = new javax.swing.JRadioButton();
        btnAnoA14 = new javax.swing.JToggleButton();
        btnFotoA14 = new javax.swing.JButton();
        rbtA25 = new javax.swing.JRadioButton();
        txpAAnot15 = new javax.swing.JScrollPane();
        txtaA15 = new javax.swing.JTextArea();
        rbtA35 = new javax.swing.JRadioButton();
        btnAnoA15 = new javax.swing.JToggleButton();
        btnFotoA15 = new javax.swing.JButton();
        rbtA26 = new javax.swing.JRadioButton();
        txpAAnot16 = new javax.swing.JScrollPane();
        txtaA16 = new javax.swing.JTextArea();
        rbtA36 = new javax.swing.JRadioButton();
        btnAnoA16 = new javax.swing.JToggleButton();
        btnFotoA16 = new javax.swing.JButton();
        rbtA27 = new javax.swing.JRadioButton();
        rbtA37 = new javax.swing.JRadioButton();
        txpAAnot17 = new javax.swing.JScrollPane();
        txtaA17 = new javax.swing.JTextArea();
        btnAnoA17 = new javax.swing.JToggleButton();
        btnFotoA17 = new javax.swing.JButton();
        rbtA28 = new javax.swing.JRadioButton();
        txpAAnot18 = new javax.swing.JScrollPane();
        txtaA18 = new javax.swing.JTextArea();
        rbtA38 = new javax.swing.JRadioButton();
        btnAnoA18 = new javax.swing.JToggleButton();
        btnFotoA18 = new javax.swing.JButton();
        rbtA29 = new javax.swing.JRadioButton();
        txpAAnot19 = new javax.swing.JScrollPane();
        txtaA19 = new javax.swing.JTextArea();
        rbtA39 = new javax.swing.JRadioButton();
        btnAnoA19 = new javax.swing.JToggleButton();
        btnFotoA19 = new javax.swing.JButton();
        rbtA30 = new javax.swing.JRadioButton();
        txpAAnot20 = new javax.swing.JScrollPane();
        txtaA20 = new javax.swing.JTextArea();
        rbtA40 = new javax.swing.JRadioButton();
        btnAnoA20 = new javax.swing.JToggleButton();
        btnFotoA20 = new javax.swing.JButton();
        rbtA41 = new javax.swing.JRadioButton();
        txpAAnot21 = new javax.swing.JScrollPane();
        txtaA21 = new javax.swing.JTextArea();
        rbtA51 = new javax.swing.JRadioButton();
        btnAnoA21 = new javax.swing.JToggleButton();
        btnFotoA21 = new javax.swing.JButton();
        rbtA42 = new javax.swing.JRadioButton();
        txpAAnot22 = new javax.swing.JScrollPane();
        txtaA22 = new javax.swing.JTextArea();
        rbtA52 = new javax.swing.JRadioButton();
        btnAnoA22 = new javax.swing.JToggleButton();
        btnFotoA22 = new javax.swing.JButton();
        rbtA43 = new javax.swing.JRadioButton();
        txpAAnot23 = new javax.swing.JScrollPane();
        txtaA23 = new javax.swing.JTextArea();
        rbtA53 = new javax.swing.JRadioButton();
        btnAnoA23 = new javax.swing.JToggleButton();
        btnFotoA23 = new javax.swing.JButton();
        rbtA44 = new javax.swing.JRadioButton();
        txpAAnot24 = new javax.swing.JScrollPane();
        txtaA24 = new javax.swing.JTextArea();
        rbtA54 = new javax.swing.JRadioButton();
        btnAnoA24 = new javax.swing.JToggleButton();
        btnFotoA24 = new javax.swing.JButton();
        rbtA45 = new javax.swing.JRadioButton();
        txpAAnot25 = new javax.swing.JScrollPane();
        txtaA25 = new javax.swing.JTextArea();
        rbtA55 = new javax.swing.JRadioButton();
        btnAnoA25 = new javax.swing.JToggleButton();
        btnFotoA25 = new javax.swing.JButton();
        rbtA46 = new javax.swing.JRadioButton();
        txpAAnot26 = new javax.swing.JScrollPane();
        txtaA26 = new javax.swing.JTextArea();
        rbtA56 = new javax.swing.JRadioButton();
        btnAnoA26 = new javax.swing.JToggleButton();
        btnFotoA26 = new javax.swing.JButton();
        rbtA47 = new javax.swing.JRadioButton();
        txpAAnot27 = new javax.swing.JScrollPane();
        txtaA27 = new javax.swing.JTextArea();
        btnAnoA27 = new javax.swing.JToggleButton();
        btnFotoA27 = new javax.swing.JButton();
        rbtA48 = new javax.swing.JRadioButton();
        txpAAnot28 = new javax.swing.JScrollPane();
        txtaA28 = new javax.swing.JTextArea();
        rbtA58 = new javax.swing.JRadioButton();
        btnAnoA28 = new javax.swing.JToggleButton();
        btnFotoA28 = new javax.swing.JButton();
        rbtA57 = new javax.swing.JRadioButton();
        rbtA49 = new javax.swing.JRadioButton();
        txpAAnot29 = new javax.swing.JScrollPane();
        txtaA29 = new javax.swing.JTextArea();
        rbtA59 = new javax.swing.JRadioButton();
        btnAnoA29 = new javax.swing.JToggleButton();
        btnFotoA29 = new javax.swing.JButton();
        rbtA50 = new javax.swing.JRadioButton();
        txpAAnot30 = new javax.swing.JScrollPane();
        txtaA30 = new javax.swing.JTextArea();
        rbtA60 = new javax.swing.JRadioButton();
        btnAnoA30 = new javax.swing.JToggleButton();
        btnFotoA30 = new javax.swing.JButton();
        rbtA61 = new javax.swing.JRadioButton();
        txpAAnot31 = new javax.swing.JScrollPane();
        txtaA31 = new javax.swing.JTextArea();
        rbtA71 = new javax.swing.JRadioButton();
        btnAnoA31 = new javax.swing.JToggleButton();
        btnFotoA31 = new javax.swing.JButton();
        rbtA62 = new javax.swing.JRadioButton();
        txpAAnot32 = new javax.swing.JScrollPane();
        txtaA32 = new javax.swing.JTextArea();
        rbtA72 = new javax.swing.JRadioButton();
        btnAnoA32 = new javax.swing.JToggleButton();
        btnFotoA32 = new javax.swing.JButton();
        btnEncPre = new javax.swing.JButton();
        rbtA63 = new javax.swing.JRadioButton();
        txpAAnot33 = new javax.swing.JScrollPane();
        txtaA33 = new javax.swing.JTextArea();
        rbtA73 = new javax.swing.JRadioButton();
        btnAnoA33 = new javax.swing.JToggleButton();
        btnFotoA33 = new javax.swing.JButton();
        rbtA64 = new javax.swing.JRadioButton();
        txpAAnot34 = new javax.swing.JScrollPane();
        txtaA34 = new javax.swing.JTextArea();
        rbtA74 = new javax.swing.JRadioButton();
        btnAnoA34 = new javax.swing.JToggleButton();
        btnFotoA34 = new javax.swing.JButton();
        rbtA65 = new javax.swing.JRadioButton();
        txpAAnot35 = new javax.swing.JScrollPane();
        txtaA35 = new javax.swing.JTextArea();
        rbtA75 = new javax.swing.JRadioButton();
        btnAnoA35 = new javax.swing.JToggleButton();
        btnFotoA35 = new javax.swing.JButton();
        rbtA66 = new javax.swing.JRadioButton();
        txpAAnot36 = new javax.swing.JScrollPane();
        txtaA36 = new javax.swing.JTextArea();
        rbtA76 = new javax.swing.JRadioButton();
        btnAnoA36 = new javax.swing.JToggleButton();
        btnFotoA36 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Refrigeração Semestral");
        setPreferredSize(new java.awt.Dimension(1007, 672));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        jPanel1.setLayout(null);

        lblFotEltMen11.setBackground(new java.awt.Color(153, 153, 153));
        lblFotEltMen11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        lblFotEltMen11.setOpaque(true);
        jPanel1.add(lblFotEltMen11);
        lblFotEltMen11.setBounds(15, 15, 240, 180);

        txtFotPre11.setEditable(false);
        txtFotPre11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtFotPre11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtFotPre11);
        txtFotPre11.setBounds(15, 210, 93, 26);

        btnSalFotPre11.setText("Salvar");
        btnSalFotPre11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnSalFotPre11.setEnabled(false);
        btnSalFotPre11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalFotPre11ActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalFotPre11);
        btnSalFotPre11.setBounds(114, 222, 75, 24);

        btnArqFotoPre.setText("Arquivo");
        btnArqFotoPre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnArqFotoPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArqFotoPreActionPerformed(evt);
            }
        });
        jPanel1.add(btnArqFotoPre);
        btnArqFotoPre.setBounds(188, 198, 75, 24);

        txtCanFot.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCanFot.setText("Cancelar");
        txtCanFot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        txtCanFot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanFotActionPerformed(evt);
            }
        });
        jPanel1.add(txtCanFot);
        txtCanFot.setBounds(188, 222, 75, 24);

        btnCamPre.setText("Camera");
        btnCamPre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnCamPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCamPreActionPerformed(evt);
            }
        });
        jPanel1.add(btnCamPre);
        btnCamPre.setBounds(114, 198, 75, 24);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(390, 90, 270, 20);

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1010, 2321));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1010, 3130));

        jPanel2.setMinimumSize(new java.awt.Dimension(1008, 3179));
        jPanel2.setPreferredSize(new java.awt.Dimension(1010, 2542));
        jPanel2.setLayout(null);

        rbtA1.setBackground(new java.awt.Color(255, 255, 255));
        grbA1.add(rbtA1);
        rbtA1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA1ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA1);
        rbtA1.setBounds(722, 392, 40, 40);

        rbtA11.setBackground(new java.awt.Color(255, 255, 255));
        grbA1.add(rbtA11);
        rbtA11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA11ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA11);
        rbtA11.setBounds(778, 392, 40, 40);

        btnAnoA1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA1);
        btnAnoA1.setBounds(836, 392, 40, 40);

        btnFotoA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA1);
        btnFotoA1.setBounds(892, 392, 40, 40);

        txpAAnot1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA1.setColumns(20);
        txtaA1.setLineWrap(true);
        txtaA1.setRows(5);
        txpAAnot1.setViewportView(txtaA1);

        jPanel2.add(txpAAnot1);
        txpAAnot1.setBounds(770, 430, 40, 10);

        txtIdPreEle.setEditable(false);
        txtIdPreEle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtIdPreEle);
        txtIdPreEle.setBounds(70, 182, 100, 28);

        txtDatIni11.setEditable(false);
        txtDatIni11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatIni11);
        txtDatIni11.setBounds(200, 182, 170, 28);

        txtDatFin.setEditable(false);
        txtDatFin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatFin);
        txtDatFin.setBounds(400, 182, 170, 28);

        txtTemPre.setEditable(false);
        txtTemPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtTemPre);
        txtTemPre.setBounds(600, 182, 170, 28);

        txtDatHor.setEditable(false);
        txtDatHor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtDatHor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatHor);
        txtDatHor.setBounds(800, 182, 140, 28);

        txtNomTec.setEditable(false);
        txtNomTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtNomTec);
        txtNomTec.setBounds(70, 243, 180, 28);

        txtSetPre.setEditable(false);
        txtSetPre.setColumns(20);
        txtSetPre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSetPre.setLineWrap(true);
        txtSetPre.setRows(5);
        txtSetPre.setText("\n");
        jScrollPane2.setViewportView(txtSetPre);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(278, 238, 184, 38);

        txtEquPr.setEditable(false);
        txtEquPr.setColumns(20);
        txtEquPr.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEquPr.setLineWrap(true);
        txtEquPr.setRows(5);
        jScrollPane3.setViewportView(txtEquPr);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(488, 238, 184, 38);

        txtCodEquPre.setEditable(false);
        txtCodEquPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtCodEquPre);
        txtCodEquPre.setBounds(700, 243, 100, 28);

        txtStaPre.setEditable(false);
        txtStaPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtStaPre);
        txtStaPre.setBounds(830, 243, 110, 28);

        rbtA12.setBackground(new java.awt.Color(255, 255, 255));
        grbA2.add(rbtA12);
        rbtA12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA12ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA12);
        rbtA12.setBounds(778, 444, 40, 40);

        rbtA2.setBackground(new java.awt.Color(255, 255, 255));
        grbA2.add(rbtA2);
        rbtA2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA2ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA2);
        rbtA2.setBounds(722, 444, 40, 40);

        btnFotoA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA2);
        btnFotoA2.setBounds(892, 444, 40, 40);

        btnAnoA2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA2);
        btnAnoA2.setBounds(836, 444, 40, 40);

        txpAAnot2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA2.setColumns(20);
        txtaA2.setLineWrap(true);
        txtaA2.setRows(5);
        txpAAnot2.setViewportView(txtaA2);

        jPanel2.add(txpAAnot2);
        txpAAnot2.setBounds(770, 482, 40, 10);

        rbtA3.setBackground(new java.awt.Color(255, 255, 255));
        grbA3.add(rbtA3);
        rbtA3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA3ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA3);
        rbtA3.setBounds(722, 497, 40, 40);

        rbtA13.setBackground(new java.awt.Color(255, 255, 255));
        grbA3.add(rbtA13);
        rbtA13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA13ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA13);
        rbtA13.setBounds(778, 497, 40, 40);

        txpAAnot3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA3.setColumns(20);
        txtaA3.setLineWrap(true);
        txtaA3.setRows(5);
        txpAAnot3.setViewportView(txtaA3);

        jPanel2.add(txpAAnot3);
        txpAAnot3.setBounds(770, 535, 40, 10);

        btnAnoA3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA3ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA3);
        btnAnoA3.setBounds(836, 497, 40, 40);

        btnFotoA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA3ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA3);
        btnFotoA3.setBounds(892, 497, 40, 40);

        rbtA4.setBackground(new java.awt.Color(255, 255, 255));
        grbA4.add(rbtA4);
        rbtA4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA4ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA4);
        rbtA4.setBounds(722, 550, 40, 40);

        txpAAnot4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA4.setColumns(20);
        txtaA4.setLineWrap(true);
        txtaA4.setRows(5);
        txpAAnot4.setViewportView(txtaA4);

        jPanel2.add(txpAAnot4);
        txpAAnot4.setBounds(770, 588, 40, 10);

        rbtA14.setBackground(new java.awt.Color(255, 255, 255));
        grbA4.add(rbtA14);
        rbtA14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA14ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA14);
        rbtA14.setBounds(778, 550, 40, 40);

        btnAnoA4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA4ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA4);
        btnAnoA4.setBounds(836, 550, 40, 40);

        btnFotoA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA4ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA4);
        btnFotoA4.setBounds(892, 550, 40, 40);

        rbtA5.setBackground(new java.awt.Color(255, 255, 255));
        grbA5.add(rbtA5);
        rbtA5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA5ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA5);
        rbtA5.setBounds(722, 604, 40, 40);

        txpAAnot5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA5.setColumns(20);
        txtaA5.setLineWrap(true);
        txtaA5.setRows(5);
        txpAAnot5.setViewportView(txtaA5);

        jPanel2.add(txpAAnot5);
        txpAAnot5.setBounds(770, 643, 40, 10);

        rbtA15.setBackground(new java.awt.Color(255, 255, 255));
        grbA5.add(rbtA15);
        rbtA15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA15ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA15);
        rbtA15.setBounds(778, 604, 40, 40);

        btnAnoA5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA5ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA5);
        btnAnoA5.setBounds(836, 604, 40, 40);

        btnFotoA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA5ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA5);
        btnFotoA5.setBounds(892, 604, 40, 40);

        rbtA6.setBackground(new java.awt.Color(255, 255, 255));
        grbA6.add(rbtA6);
        rbtA6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA6ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA6);
        rbtA6.setBounds(722, 657, 40, 40);

        txpAAnot6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA6.setColumns(20);
        txtaA6.setLineWrap(true);
        txtaA6.setRows(5);
        txpAAnot6.setViewportView(txtaA6);

        jPanel2.add(txpAAnot6);
        txpAAnot6.setBounds(770, 696, 40, 10);

        rbtA16.setBackground(new java.awt.Color(255, 255, 255));
        grbA6.add(rbtA16);
        rbtA16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA16ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA16);
        rbtA16.setBounds(778, 657, 40, 40);

        btnAnoA6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA6ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA6);
        btnAnoA6.setBounds(836, 657, 40, 40);

        btnFotoA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA6ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA6);
        btnFotoA6.setBounds(892, 657, 40, 40);

        rbtA7.setBackground(new java.awt.Color(255, 255, 255));
        grbA7.add(rbtA7);
        rbtA7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA7ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA7);
        rbtA7.setBounds(722, 709, 40, 40);

        txpAAnot7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA7.setColumns(20);
        txtaA7.setLineWrap(true);
        txtaA7.setRows(5);
        txpAAnot7.setViewportView(txtaA7);

        jPanel2.add(txpAAnot7);
        txpAAnot7.setBounds(770, 749, 40, 10);

        rbtA17.setBackground(new java.awt.Color(255, 255, 255));
        grbA7.add(rbtA17);
        rbtA17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA17ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA17);
        rbtA17.setBounds(778, 709, 40, 40);

        btnAnoA7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA7ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA7);
        btnAnoA7.setBounds(836, 709, 40, 40);

        btnFotoA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA7ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA7);
        btnFotoA7.setBounds(892, 709, 40, 40);

        rbtA8.setBackground(new java.awt.Color(255, 255, 255));
        grbA8.add(rbtA8);
        rbtA8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA8ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA8);
        rbtA8.setBounds(722, 762, 40, 40);

        txpAAnot8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA8.setColumns(20);
        txtaA8.setLineWrap(true);
        txtaA8.setRows(5);
        txpAAnot8.setViewportView(txtaA8);

        jPanel2.add(txpAAnot8);
        txpAAnot8.setBounds(770, 800, 40, 10);

        rbtA18.setBackground(new java.awt.Color(255, 255, 255));
        grbA8.add(rbtA18);
        rbtA18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA18ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA18);
        rbtA18.setBounds(778, 762, 40, 40);

        btnAnoA8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA8ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA8);
        btnAnoA8.setBounds(836, 762, 40, 40);

        btnFotoA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA8ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA8);
        btnFotoA8.setBounds(892, 762, 40, 40);

        rbtA9.setBackground(new java.awt.Color(255, 255, 255));
        grbA9.add(rbtA9);
        rbtA9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA9ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA9);
        rbtA9.setBounds(722, 815, 40, 40);

        txpAAnot9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA9.setColumns(20);
        txtaA9.setLineWrap(true);
        txtaA9.setRows(5);
        txpAAnot9.setViewportView(txtaA9);

        jPanel2.add(txpAAnot9);
        txpAAnot9.setBounds(770, 854, 40, 10);

        rbtA19.setBackground(new java.awt.Color(255, 255, 255));
        grbA9.add(rbtA19);
        rbtA19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA19ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA19);
        rbtA19.setBounds(778, 815, 40, 40);

        btnAnoA9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA9ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA9);
        btnAnoA9.setBounds(836, 815, 40, 40);

        btnFotoA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA9ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA9);
        btnFotoA9.setBounds(892, 815, 40, 40);

        rbtA10.setBackground(new java.awt.Color(255, 255, 255));
        grbA10.add(rbtA10);
        rbtA10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA10ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA10);
        rbtA10.setBounds(722, 868, 40, 40);

        txpAAnot10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA10.setColumns(20);
        txtaA10.setLineWrap(true);
        txtaA10.setRows(5);
        txpAAnot10.setViewportView(txtaA10);

        jPanel2.add(txpAAnot10);
        txpAAnot10.setBounds(770, 907, 40, 10);

        rbtA20.setBackground(new java.awt.Color(255, 255, 255));
        grbA10.add(rbtA20);
        rbtA20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA20ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA20);
        rbtA20.setBounds(778, 868, 40, 40);

        btnAnoA10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA10ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA10);
        btnAnoA10.setBounds(836, 868, 40, 40);

        btnFotoA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA10ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA10);
        btnFotoA10.setBounds(892, 868, 40, 40);

        rbtA21.setBackground(new java.awt.Color(255, 255, 255));
        grbA11.add(rbtA21);
        rbtA21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA21ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA21);
        rbtA21.setBounds(722, 922, 40, 40);

        rbtA31.setBackground(new java.awt.Color(255, 255, 255));
        grbA11.add(rbtA31);
        rbtA31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA31ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA31);
        rbtA31.setBounds(778, 922, 40, 40);

        txpAAnot11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA11.setColumns(20);
        txtaA11.setLineWrap(true);
        txtaA11.setRows(5);
        txpAAnot11.setViewportView(txtaA11);

        jPanel2.add(txpAAnot11);
        txpAAnot11.setBounds(770, 960, 40, 10);

        btnAnoA11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA11ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA11);
        btnAnoA11.setBounds(836, 922, 40, 40);

        btnFotoA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA11ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA11);
        btnFotoA11.setBounds(892, 922, 40, 40);

        rbtA22.setBackground(new java.awt.Color(255, 255, 255));
        grbA12.add(rbtA22);
        rbtA22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA22ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA22);
        rbtA22.setBounds(722, 975, 40, 40);

        txpAAnot12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA12.setColumns(20);
        txtaA12.setLineWrap(true);
        txtaA12.setRows(5);
        txpAAnot12.setViewportView(txtaA12);

        jPanel2.add(txpAAnot12);
        txpAAnot12.setBounds(770, 1014, 40, 10);

        rbtA32.setBackground(new java.awt.Color(255, 255, 255));
        grbA12.add(rbtA32);
        rbtA32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA32ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA32);
        rbtA32.setBounds(778, 975, 40, 40);

        btnAnoA12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA12ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA12);
        btnAnoA12.setBounds(836, 975, 40, 40);

        btnFotoA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA12ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA12);
        btnFotoA12.setBounds(892, 975, 40, 40);

        rbtA23.setBackground(new java.awt.Color(255, 255, 255));
        grbA13.add(rbtA23);
        rbtA23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA23ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA23);
        rbtA23.setBounds(722, 1028, 40, 40);

        txpAAnot13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA13.setColumns(20);
        txtaA13.setLineWrap(true);
        txtaA13.setRows(5);
        txpAAnot13.setViewportView(txtaA13);

        jPanel2.add(txpAAnot13);
        txpAAnot13.setBounds(770, 1067, 40, 10);

        rbtA33.setBackground(new java.awt.Color(255, 255, 255));
        grbA13.add(rbtA33);
        rbtA33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA33ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA33);
        rbtA33.setBounds(778, 1028, 40, 40);

        btnAnoA13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA13ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA13);
        btnAnoA13.setBounds(836, 1028, 40, 40);

        btnFotoA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA13ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA13);
        btnFotoA13.setBounds(892, 1028, 40, 40);

        rbtA24.setBackground(new java.awt.Color(255, 255, 255));
        grbA14.add(rbtA24);
        rbtA24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA24ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA24);
        rbtA24.setBounds(722, 1081, 40, 40);

        txpAAnot14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA14.setColumns(20);
        txtaA14.setLineWrap(true);
        txtaA14.setRows(5);
        txpAAnot14.setViewportView(txtaA14);

        jPanel2.add(txpAAnot14);
        txpAAnot14.setBounds(770, 1120, 40, 10);

        rbtA34.setBackground(new java.awt.Color(255, 255, 255));
        grbA14.add(rbtA34);
        rbtA34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA34ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA34);
        rbtA34.setBounds(778, 1081, 40, 40);

        btnAnoA14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA14ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA14);
        btnAnoA14.setBounds(836, 1081, 40, 40);

        btnFotoA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA14ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA14);
        btnFotoA14.setBounds(892, 1081, 40, 40);

        rbtA25.setBackground(new java.awt.Color(255, 255, 255));
        grbA15.add(rbtA25);
        rbtA25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA25ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA25);
        rbtA25.setBounds(722, 1134, 40, 40);

        txpAAnot15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA15.setColumns(20);
        txtaA15.setLineWrap(true);
        txtaA15.setRows(5);
        txpAAnot15.setViewportView(txtaA15);

        jPanel2.add(txpAAnot15);
        txpAAnot15.setBounds(770, 1172, 40, 10);

        rbtA35.setBackground(new java.awt.Color(255, 255, 255));
        grbA15.add(rbtA35);
        rbtA35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA35ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA35);
        rbtA35.setBounds(778, 1134, 40, 40);

        btnAnoA15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA15ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA15);
        btnAnoA15.setBounds(836, 1134, 40, 40);

        btnFotoA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA15ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA15);
        btnFotoA15.setBounds(892, 1134, 40, 40);

        rbtA26.setBackground(new java.awt.Color(255, 255, 255));
        grbA16.add(rbtA26);
        rbtA26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA26ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA26);
        rbtA26.setBounds(722, 1187, 40, 40);

        txpAAnot16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA16.setColumns(20);
        txtaA16.setLineWrap(true);
        txtaA16.setRows(5);
        txpAAnot16.setViewportView(txtaA16);

        jPanel2.add(txpAAnot16);
        txpAAnot16.setBounds(770, 1226, 40, 10);

        rbtA36.setBackground(new java.awt.Color(255, 255, 255));
        grbA16.add(rbtA36);
        rbtA36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA36ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA36);
        rbtA36.setBounds(778, 1187, 40, 40);

        btnAnoA16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA16ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA16);
        btnAnoA16.setBounds(836, 1187, 40, 40);

        btnFotoA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA16ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA16);
        btnFotoA16.setBounds(892, 1187, 40, 40);

        rbtA27.setBackground(new java.awt.Color(255, 255, 255));
        grbA17.add(rbtA27);
        rbtA27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA27ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA27);
        rbtA27.setBounds(722, 1240, 40, 40);

        rbtA37.setBackground(new java.awt.Color(255, 255, 255));
        grbA17.add(rbtA37);
        rbtA37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA37ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA37);
        rbtA37.setBounds(778, 1240, 40, 40);

        txpAAnot17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA17.setColumns(20);
        txtaA17.setLineWrap(true);
        txtaA17.setRows(5);
        txpAAnot17.setViewportView(txtaA17);

        jPanel2.add(txpAAnot17);
        txpAAnot17.setBounds(770, 1278, 40, 10);

        btnAnoA17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA17ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA17);
        btnAnoA17.setBounds(836, 1240, 40, 40);

        btnFotoA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA17ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA17);
        btnFotoA17.setBounds(892, 1240, 40, 40);

        rbtA28.setBackground(new java.awt.Color(255, 255, 255));
        grbA18.add(rbtA28);
        rbtA28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA28ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA28);
        rbtA28.setBounds(722, 1292, 40, 40);

        txpAAnot18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA18.setColumns(20);
        txtaA18.setLineWrap(true);
        txtaA18.setRows(5);
        txpAAnot18.setViewportView(txtaA18);

        jPanel2.add(txpAAnot18);
        txpAAnot18.setBounds(770, 1330, 40, 10);

        rbtA38.setBackground(new java.awt.Color(255, 255, 255));
        grbA18.add(rbtA38);
        rbtA38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA38.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA38ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA38);
        rbtA38.setBounds(778, 1292, 40, 40);

        btnAnoA18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA18ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA18);
        btnAnoA18.setBounds(836, 1291, 40, 40);

        btnFotoA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA18ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA18);
        btnFotoA18.setBounds(892, 1292, 40, 40);

        rbtA29.setBackground(new java.awt.Color(255, 255, 255));
        grbA19.add(rbtA29);
        rbtA29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA29ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA29);
        rbtA29.setBounds(722, 1346, 40, 40);

        txpAAnot19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA19.setColumns(20);
        txtaA19.setLineWrap(true);
        txtaA19.setRows(5);
        txpAAnot19.setViewportView(txtaA19);

        jPanel2.add(txpAAnot19);
        txpAAnot19.setBounds(770, 1386, 40, 10);

        rbtA39.setBackground(new java.awt.Color(255, 255, 255));
        grbA19.add(rbtA39);
        rbtA39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA39.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA39ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA39);
        rbtA39.setBounds(778, 1346, 40, 40);

        btnAnoA19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA19ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA19);
        btnAnoA19.setBounds(836, 1346, 40, 40);

        btnFotoA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA19ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA19);
        btnFotoA19.setBounds(892, 1346, 40, 40);

        rbtA30.setBackground(new java.awt.Color(255, 255, 255));
        gbrA20.add(rbtA30);
        rbtA30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA30ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA30);
        rbtA30.setBounds(722, 1398, 40, 40);

        txpAAnot20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA20.setColumns(20);
        txtaA20.setLineWrap(true);
        txtaA20.setRows(5);
        txpAAnot20.setViewportView(txtaA20);

        jPanel2.add(txpAAnot20);
        txpAAnot20.setBounds(770, 1438, 40, 10);

        rbtA40.setBackground(new java.awt.Color(255, 255, 255));
        gbrA20.add(rbtA40);
        rbtA40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA40.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA40ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA40);
        rbtA40.setBounds(778, 1398, 40, 40);

        btnAnoA20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA20ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA20);
        btnAnoA20.setBounds(836, 1398, 40, 40);

        btnFotoA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA20ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA20);
        btnFotoA20.setBounds(892, 1398, 40, 40);

        rbtA41.setBackground(new java.awt.Color(255, 255, 255));
        gbrA21.add(rbtA41);
        rbtA41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA41.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA41ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA41);
        rbtA41.setBounds(722, 1452, 40, 40);

        txpAAnot21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA21.setColumns(20);
        txtaA21.setLineWrap(true);
        txtaA21.setRows(5);
        txpAAnot21.setViewportView(txtaA21);

        jPanel2.add(txpAAnot21);
        txpAAnot21.setBounds(770, 1492, 40, 10);

        rbtA51.setBackground(new java.awt.Color(255, 255, 255));
        gbrA21.add(rbtA51);
        rbtA51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA51ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA51);
        rbtA51.setBounds(778, 1452, 40, 40);

        btnAnoA21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA21ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA21);
        btnAnoA21.setBounds(836, 1452, 40, 40);

        btnFotoA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA21ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA21);
        btnFotoA21.setBounds(892, 1452, 40, 40);

        rbtA42.setBackground(new java.awt.Color(255, 255, 255));
        gbrA22.add(rbtA42);
        rbtA42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA42.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA42ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA42);
        rbtA42.setBounds(722, 1505, 40, 40);

        txpAAnot22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA22.setColumns(20);
        txtaA22.setLineWrap(true);
        txtaA22.setRows(5);
        txpAAnot22.setViewportView(txtaA22);

        jPanel2.add(txpAAnot22);
        txpAAnot22.setBounds(770, 1544, 40, 10);

        rbtA52.setBackground(new java.awt.Color(255, 255, 255));
        gbrA22.add(rbtA52);
        rbtA52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA52.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA52ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA52);
        rbtA52.setBounds(778, 1505, 40, 40);

        btnAnoA22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA22ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA22);
        btnAnoA22.setBounds(836, 1505, 40, 40);

        btnFotoA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA22ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA22);
        btnFotoA22.setBounds(892, 1505, 40, 40);

        rbtA43.setBackground(new java.awt.Color(255, 255, 255));
        gbrA23.add(rbtA43);
        rbtA43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA43.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA43ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA43);
        rbtA43.setBounds(722, 1557, 40, 40);

        txpAAnot23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA23.setColumns(20);
        txtaA23.setLineWrap(true);
        txtaA23.setRows(5);
        txpAAnot23.setViewportView(txtaA23);

        jPanel2.add(txpAAnot23);
        txpAAnot23.setBounds(770, 1596, 40, 10);

        rbtA53.setBackground(new java.awt.Color(255, 255, 255));
        gbrA23.add(rbtA53);
        rbtA53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA53.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA53ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA53);
        rbtA53.setBounds(778, 1557, 40, 40);

        btnAnoA23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA23ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA23);
        btnAnoA23.setBounds(836, 1557, 40, 40);

        btnFotoA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA23ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA23);
        btnFotoA23.setBounds(892, 1557, 40, 40);

        rbtA44.setBackground(new java.awt.Color(255, 255, 255));
        gbrA24.add(rbtA44);
        rbtA44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA44.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA44ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA44);
        rbtA44.setBounds(722, 1610, 40, 40);

        txpAAnot24.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA24.setColumns(20);
        txtaA24.setLineWrap(true);
        txtaA24.setRows(5);
        txpAAnot24.setViewportView(txtaA24);

        jPanel2.add(txpAAnot24);
        txpAAnot24.setBounds(770, 1650, 40, 10);

        rbtA54.setBackground(new java.awt.Color(255, 255, 255));
        gbrA24.add(rbtA54);
        rbtA54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA54.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA54ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA54);
        rbtA54.setBounds(778, 1610, 40, 40);

        btnAnoA24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA24ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA24);
        btnAnoA24.setBounds(836, 1610, 40, 40);

        btnFotoA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA24ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA24);
        btnFotoA24.setBounds(892, 1610, 40, 40);

        rbtA45.setBackground(new java.awt.Color(255, 255, 255));
        gbrA25.add(rbtA45);
        rbtA45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA45.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA45ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA45);
        rbtA45.setBounds(722, 1664, 40, 40);

        txpAAnot25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA25.setColumns(20);
        txtaA25.setLineWrap(true);
        txtaA25.setRows(5);
        txpAAnot25.setViewportView(txtaA25);

        jPanel2.add(txpAAnot25);
        txpAAnot25.setBounds(770, 1702, 40, 10);

        rbtA55.setBackground(new java.awt.Color(255, 255, 255));
        gbrA25.add(rbtA55);
        rbtA55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA55.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA55ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA55);
        rbtA55.setBounds(778, 1664, 40, 40);

        btnAnoA25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA25ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA25);
        btnAnoA25.setBounds(836, 1664, 40, 40);

        btnFotoA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA25ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA25);
        btnFotoA25.setBounds(892, 1664, 40, 40);

        rbtA46.setBackground(new java.awt.Color(255, 255, 255));
        gbrA26.add(rbtA46);
        rbtA46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA46.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA46ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA46);
        rbtA46.setBounds(722, 1716, 40, 40);

        txpAAnot26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA26.setColumns(20);
        txtaA26.setLineWrap(true);
        txtaA26.setRows(5);
        txpAAnot26.setViewportView(txtaA26);

        jPanel2.add(txpAAnot26);
        txpAAnot26.setBounds(770, 1754, 40, 10);

        rbtA56.setBackground(new java.awt.Color(255, 255, 255));
        gbrA26.add(rbtA56);
        rbtA56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA56.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA56ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA56);
        rbtA56.setBounds(778, 1716, 40, 40);

        btnAnoA26.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA26ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA26);
        btnAnoA26.setBounds(836, 1716, 40, 40);

        btnFotoA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA26ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA26);
        btnFotoA26.setBounds(892, 1716, 40, 40);

        rbtA47.setBackground(new java.awt.Color(255, 255, 255));
        gbrA27.add(rbtA47);
        rbtA47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA47.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA47ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA47);
        rbtA47.setBounds(722, 1770, 40, 40);

        txpAAnot27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA27.setColumns(20);
        txtaA27.setLineWrap(true);
        txtaA27.setRows(5);
        txpAAnot27.setViewportView(txtaA27);

        jPanel2.add(txpAAnot27);
        txpAAnot27.setBounds(770, 1810, 40, 10);

        btnAnoA27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA27ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA27);
        btnAnoA27.setBounds(836, 1770, 40, 40);

        btnFotoA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA27ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA27);
        btnFotoA27.setBounds(892, 1770, 40, 40);

        rbtA48.setBackground(new java.awt.Color(255, 255, 255));
        gbrA28.add(rbtA48);
        rbtA48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA48.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA48ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA48);
        rbtA48.setBounds(722, 1822, 40, 40);

        txpAAnot28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA28.setColumns(20);
        txtaA28.setLineWrap(true);
        txtaA28.setRows(5);
        txpAAnot28.setViewportView(txtaA28);

        jPanel2.add(txpAAnot28);
        txpAAnot28.setBounds(770, 1861, 40, 10);

        rbtA58.setBackground(new java.awt.Color(255, 255, 255));
        gbrA28.add(rbtA58);
        rbtA58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA58.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA58ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA58);
        rbtA58.setBounds(778, 1822, 40, 40);

        btnAnoA28.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA28ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA28);
        btnAnoA28.setBounds(836, 1822, 40, 40);

        btnFotoA28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA28ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA28);
        btnFotoA28.setBounds(892, 1822, 40, 40);

        rbtA57.setBackground(new java.awt.Color(255, 255, 255));
        gbrA27.add(rbtA57);
        rbtA57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA57.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA57ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA57);
        rbtA57.setBounds(778, 1770, 40, 40);

        rbtA49.setBackground(new java.awt.Color(255, 255, 255));
        gbrA29.add(rbtA49);
        rbtA49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA49.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA49ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA49);
        rbtA49.setBounds(722, 1876, 40, 40);

        txpAAnot29.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA29.setColumns(20);
        txtaA29.setLineWrap(true);
        txtaA29.setRows(5);
        txpAAnot29.setViewportView(txtaA29);

        jPanel2.add(txpAAnot29);
        txpAAnot29.setBounds(770, 1915, 40, 10);

        rbtA59.setBackground(new java.awt.Color(255, 255, 255));
        gbrA29.add(rbtA59);
        rbtA59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA59.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA59ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA59);
        rbtA59.setBounds(778, 1876, 40, 40);

        btnAnoA29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA29ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA29);
        btnAnoA29.setBounds(836, 1876, 40, 40);

        btnFotoA29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA29ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA29);
        btnFotoA29.setBounds(892, 1876, 40, 40);

        rbtA50.setBackground(new java.awt.Color(255, 255, 255));
        gbrA30.add(rbtA50);
        rbtA50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA50.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA50ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA50);
        rbtA50.setBounds(722, 1929, 40, 40);

        txpAAnot30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA30.setColumns(20);
        txtaA30.setLineWrap(true);
        txtaA30.setRows(5);
        txpAAnot30.setViewportView(txtaA30);

        jPanel2.add(txpAAnot30);
        txpAAnot30.setBounds(770, 1968, 40, 10);

        rbtA60.setBackground(new java.awt.Color(255, 255, 255));
        gbrA30.add(rbtA60);
        rbtA60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA60.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA60ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA60);
        rbtA60.setBounds(778, 1929, 40, 40);

        btnAnoA30.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA30ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA30);
        btnAnoA30.setBounds(836, 1929, 40, 40);

        btnFotoA30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA30ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA30);
        btnFotoA30.setBounds(892, 1929, 40, 40);

        rbtA61.setBackground(new java.awt.Color(255, 255, 255));
        gbrA31.add(rbtA61);
        rbtA61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA61.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA61ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA61);
        rbtA61.setBounds(722, 1982, 40, 40);

        txpAAnot31.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA31.setColumns(20);
        txtaA31.setLineWrap(true);
        txtaA31.setRows(5);
        txpAAnot31.setViewportView(txtaA31);

        jPanel2.add(txpAAnot31);
        txpAAnot31.setBounds(770, 2021, 40, 10);

        rbtA71.setBackground(new java.awt.Color(255, 255, 255));
        gbrA31.add(rbtA71);
        rbtA71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA71.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA71ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA71);
        rbtA71.setBounds(778, 1982, 40, 40);

        btnAnoA31.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA31ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA31);
        btnAnoA31.setBounds(836, 1982, 40, 40);

        btnFotoA31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA31ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA31);
        btnFotoA31.setBounds(892, 1982, 40, 40);

        rbtA62.setBackground(new java.awt.Color(255, 255, 255));
        gbrA32.add(rbtA62);
        rbtA62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA62.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA62ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA62);
        rbtA62.setBounds(722, 2034, 40, 40);

        txpAAnot32.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA32.setColumns(20);
        txtaA32.setLineWrap(true);
        txtaA32.setRows(5);
        txpAAnot32.setViewportView(txtaA32);

        jPanel2.add(txpAAnot32);
        txpAAnot32.setBounds(770, 2073, 40, 10);

        rbtA72.setBackground(new java.awt.Color(255, 255, 255));
        gbrA32.add(rbtA72);
        rbtA72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA72.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA72ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA72);
        rbtA72.setBounds(778, 2034, 40, 40);

        btnAnoA32.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA32ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA32);
        btnAnoA32.setBounds(836, 2034, 40, 40);

        btnFotoA32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA32ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA32);
        btnFotoA32.setBounds(892, 2034, 40, 40);

        btnEncPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48763_edit_edit_file_file.png"))); // NOI18N
        btnEncPre.setToolTipText("Encerrar Preventiva");
        btnEncPre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEncPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncPreActionPerformed(evt);
            }
        });
        jPanel2.add(btnEncPre);
        btnEncPre.setBounds(834, 2402, 64, 64);

        rbtA63.setBackground(new java.awt.Color(255, 255, 255));
        grbA33.add(rbtA63);
        rbtA63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA63.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA63ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA63);
        rbtA63.setBounds(722, 2088, 40, 40);

        txpAAnot33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA33.setColumns(20);
        txtaA33.setLineWrap(true);
        txtaA33.setRows(5);
        txpAAnot33.setViewportView(txtaA33);

        jPanel2.add(txpAAnot33);
        txpAAnot33.setBounds(770, 2126, 40, 10);

        rbtA73.setBackground(new java.awt.Color(255, 255, 255));
        grbA33.add(rbtA73);
        rbtA73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA73.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA73ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA73);
        rbtA73.setBounds(778, 2088, 40, 40);

        btnAnoA33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA33ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA33);
        btnAnoA33.setBounds(836, 2088, 40, 40);

        btnFotoA33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA33ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA33);
        btnFotoA33.setBounds(892, 2088, 40, 40);

        rbtA64.setBackground(new java.awt.Color(255, 255, 255));
        grbA34.add(rbtA64);
        rbtA64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA64.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA64ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA64);
        rbtA64.setBounds(722, 2141, 40, 40);

        txpAAnot34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA34.setColumns(20);
        txtaA34.setLineWrap(true);
        txtaA34.setRows(5);
        txpAAnot34.setViewportView(txtaA34);

        jPanel2.add(txpAAnot34);
        txpAAnot34.setBounds(770, 2179, 40, 10);

        rbtA74.setBackground(new java.awt.Color(255, 255, 255));
        grbA34.add(rbtA74);
        rbtA74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA74.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA74ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA74);
        rbtA74.setBounds(778, 2141, 40, 40);

        btnAnoA34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA34ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA34);
        btnAnoA34.setBounds(836, 2141, 40, 40);

        btnFotoA34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA34ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA34);
        btnFotoA34.setBounds(892, 2141, 40, 40);

        rbtA65.setBackground(new java.awt.Color(255, 255, 255));
        grbA35.add(rbtA65);
        rbtA65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA65.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA65ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA65);
        rbtA65.setBounds(722, 2194, 40, 40);

        txpAAnot35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA35.setColumns(20);
        txtaA35.setLineWrap(true);
        txtaA35.setRows(5);
        txpAAnot35.setViewportView(txtaA35);

        jPanel2.add(txpAAnot35);
        txpAAnot35.setBounds(770, 2232, 40, 10);

        rbtA75.setBackground(new java.awt.Color(255, 255, 255));
        grbA35.add(rbtA75);
        rbtA75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA75.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA75ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA75);
        rbtA75.setBounds(778, 2194, 40, 40);

        btnAnoA35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA35ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA35);
        btnAnoA35.setBounds(836, 2194, 40, 40);

        btnFotoA35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA35ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA35);
        btnFotoA35.setBounds(892, 2194, 40, 40);

        rbtA66.setBackground(new java.awt.Color(255, 255, 255));
        grbA36.add(rbtA66);
        rbtA66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA66.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA66ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA66);
        rbtA66.setBounds(722, 2247, 40, 40);

        txpAAnot36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA36.setColumns(20);
        txtaA36.setLineWrap(true);
        txtaA36.setRows(5);
        txpAAnot36.setViewportView(txtaA36);

        jPanel2.add(txpAAnot36);
        txpAAnot36.setBounds(770, 2285, 40, 10);

        rbtA76.setBackground(new java.awt.Color(255, 255, 255));
        grbA36.add(rbtA76);
        rbtA76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA76.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA76ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA76);
        rbtA76.setBounds(778, 2247, 40, 40);

        btnAnoA36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA36ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA36);
        btnAnoA36.setBounds(836, 2247, 40, 40);

        btnFotoA36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA36ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA36);
        btnFotoA36.setBounds(892, 2247, 40, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Refrigeração Semestral.png"))); // NOI18N
        jLabel1.setMinimumSize(new java.awt.Dimension(1012, 2318));
        jLabel1.setPreferredSize(new java.awt.Dimension(1012, 2318));
        jPanel2.add(jLabel1);
        jLabel1.setBounds(0, 0, 1010, 2542);

        jScrollPane1.setViewportView(jPanel2);
        jPanel2.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(-4, -3, 1004, 645);
        jScrollPane1.getAccessibleContext().setAccessibleName("");

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened
     *
     * Acionado automaticamente quando a janela interna (`JInternalFrame`) é
     * aberta.
     *
     * Funcionalidades: 1. **Inicializa os botões de rádio** `Não Conforme`: -
     * Cria um array com 36 botões de rádio (`JRadioButton`) representando itens
     * específicos. - Marca todos como selecionados (`setSelected(true)`). -
     * Define a situação de cada item como `"Não Conforme"` e armazena em um
     * array de `String`.
     *
     * 2. **Atribui valores às variáveis individuais de situação**: - Copia os
     * valores do array `situacoes` para variáveis como `situacao`, `situacao1`,
     * ..., `situacao35`. - Isso garante compatibilidade com outras partes do
     * código que usam variáveis separadas.
     *
     * 3. **Aplica o validador de teclado aos campos de texto**: - Para cada
     * campo `txtaA1` até `txtaA36`, adiciona o `KeyAdapter` `validadorTeclado`.
     * - O validador restringe a entrada a caracteres permitidos e limita o
     * texto a 200 caracteres. - Utiliza reflexão (`getDeclaredField`) para
     * acessar dinamicamente os campos. - Em caso de erro, exibe uma mensagem de
     * alerta via `JOptionPane`.
     *
     * Esse método garante que, ao abrir a janela, todos os itens estejam
     * inicialmente marcados como "Não Conforme" e que os campos de observação
     * estejam protegidos contra entradas inválidas.
     *
     * @param evt o evento gerado pela abertura da janela interna
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        // Array de botões "Não Conforme" correspondentes aos itens A1 a A36
        JRadioButton[] botoesNaoConforme = {
            rbtA1, rbtA2, rbtA3, rbtA4, rbtA5, rbtA6, rbtA7, rbtA8, rbtA9, rbtA10,
            rbtA21, rbtA22, rbtA23, rbtA24, rbtA25, rbtA26, rbtA27, rbtA28, rbtA29, rbtA30,
            rbtA41, rbtA42, rbtA43, rbtA44, rbtA45, rbtA46, rbtA47, rbtA48, rbtA49, rbtA50,
            rbtA61, rbtA62, rbtA63, rbtA64, rbtA65, rbtA66
        };

        // Array de variáveis de situação associadas aos itens A1 a A36
        String[] situacoes = new String[36];

        // Laço para configurar todos os itens como "Não Conforme"
        for (int i = 0; i < botoesNaoConforme.length; i++) {
            botoesNaoConforme[i].setSelected(true);
            situacoes[i] = "Não Conforme";
        }

        // Atribui os valores das situações às variáveis originais
        situacao = situacoes[0];
        situacao1 = situacoes[1];
        situacao2 = situacoes[2];
        situacao3 = situacoes[3];
        situacao4 = situacoes[4];
        situacao5 = situacoes[5];
        situacao6 = situacoes[6];
        situacao7 = situacoes[7];
        situacao8 = situacoes[8];
        situacao9 = situacoes[9];
        situacao10 = situacoes[10];
        situacao11 = situacoes[11];
        situacao12 = situacoes[12];
        situacao13 = situacoes[13];
        situacao14 = situacoes[14];
        situacao15 = situacoes[15];
        situacao16 = situacoes[16];
        situacao17 = situacoes[17];
        situacao18 = situacoes[18];
        situacao19 = situacoes[19];
        situacao20 = situacoes[20];
        situacao21 = situacoes[21];
        situacao22 = situacoes[22];
        situacao23 = situacoes[23];
        situacao24 = situacoes[24];
        situacao25 = situacoes[25];
        situacao26 = situacoes[26];
        situacao27 = situacoes[27];
        situacao28 = situacoes[28];
        situacao29 = situacoes[29];
        situacao30 = situacoes[30];
        situacao31 = situacoes[31];
        situacao32 = situacoes[32];
        situacao33 = situacoes[33];
        situacao34 = situacoes[34];
        situacao35 = situacoes[35];

        for (int i = 1; i <= 36; i++) {
            try {
                JTextArea campo = (JTextArea) this.getClass().getDeclaredField("txtaA" + i).get(this);
                campo.addKeyListener(validadorTeclado);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                JOptionPane.showMessageDialog(null, "Erro" + e.getMessage(), "Atenção", 3);
            }
        }

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     *
     * Acionado quando o botão de rádio "Conforme" do item A1 é selecionado.
     *
     *
     * Este método define a variável {situacao} como {"Conforme"}, indicando que
     * o item A1 está em conformidade com os critérios de verificação.
     *
     * Essa atribuição é usada posteriormente para atualização no banco de dados
     * e geração de relatórios da preventiva.
     *
     * @param evt o evento de ação que desencadeou a chamada (seleção do botão
     * de rádio)
     */
    private void rbtA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA11ActionPerformed

        situacao = "Conforme";
    }//GEN-LAST:event_rbtA11ActionPerformed

    /**
     * Acionado quando o botão de rádio "Não Conforme" do item A1 é selecionado.
     *
     * Este método define a variável situacao como "Não Conforme", indicando que
     * o item A1 não está em conformidade com os critérios estabelecidos.
     *
     * Essa informação será utilizada posteriormente para persistência no banco
     * de dados ou geração de relatórios da preventiva.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA1ActionPerformed

        situacao = "Não Conforme";
    }//GEN-LAST:event_rbtA1ActionPerformed

    /**
     * Método `btnAnoA1ActionPerformed`. Acionado quando o botão de anotação do
     * item A1 é clicado.
     *
     * Este método verifica se o botão btnAnoA1 está selecionado. Se estiver,
     * torna visível o campo de texto txpAAnot1 e define seu tamanho para
     * 170x100 pixels. Caso contrário, oculta o campo de anotação.
     *
     * Esse comportamento permite ao usuário exibir ou ocultar observações
     * adicionais relacionadas ao item A1 da checklist de forma dinâmica.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A1
     */
    private void btnAnoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA1ActionPerformed

        if (btnAnoA1.isSelected()) {
            txpAAnot1.setVisible(true);
            txpAAnot1.setSize(170, 100);
            txtaA1.requestFocus();
        } else {
            txpAAnot1.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA1ActionPerformed

    /**
     * Método `btnFotoA1ActionPerformed`. Acionado ao clicar no botão de foto do
     * item A1.
     *
     * Define o prefixo do nome da imagem como "A1 - " e o atribui à variável
     * nome_foto. Em seguida, exibe o painel jPanel1 e ajusta seu tamanho para
     * 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A1.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A1
     */
    private void btnFotoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA1ActionPerformed

        nome1 = "A1 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);

    }//GEN-LAST:event_btnFotoA1ActionPerformed

    /**
     * Método btnEncPreActionPerformed Acionado ao clicar no botão de
     * encerramento da preventiva.
     *
     * Exibe uma caixa de diálogo de confirmação. Se o usuário confirmar: -
     * Define a data/hora atual formatada no campo txtDatFin - Chama o método
     * cauc_tempo para calcular o tempo total - Atualiza o status da preventiva
     * para "Encerrada" - Chama o método iniciar_prev para salvar os dados
     * finais
     *
     * @param evt o evento de ação gerado pelo clique no botão de encerramento
     */
    private void btnEncPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncPreActionPerformed

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja Ecerrar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            ZonedDateTime zdtNow = ZonedDateTime.now();
            String format = data_hora.format(zdtNow);
            txtDatFin.setText(format);
            cauc_tempo();

            String status = "Encerrada";
            txtStaPre.setText(status);
            iniciar_prev();
        }
    }//GEN-LAST:event_btnEncPreActionPerformed

    /**
     * Método rbtA12ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A2 é selecionado.
     *
     * Define a variável situacao1 como "Conforme", indicando que o item A2 está
     * em conformidade com os critérios estabelecidos.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA12ActionPerformed

        situacao1 = "Conforme";
    }//GEN-LAST:event_rbtA12ActionPerformed

    /**
     * Método rbtA2ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A2 é selecionado.
     *
     * Define a variável situacao1 como "Não Conforme", indicando que o item A2
     * não está em conformidade com os critérios estabelecidos.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA2ActionPerformed

        situacao1 = "Não Conforme";
    }//GEN-LAST:event_rbtA2ActionPerformed

    /**
     * Método btnAnoA2ActionPerformed Acionado ao clicar no botão de anotação do
     * item A2.
     *
     * Exibe ou oculta o campo de anotação txpAAnot2 com base no estado do botão
     * btnAnoA2. Se selecionado, o campo é exibido com tamanho 170x100 pixels;
     * caso contrário, é ocultado.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A2
     */
    private void btnAnoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA2ActionPerformed

        if (btnAnoA2.isSelected()) {
            txpAAnot2.setVisible(true);
            txpAAnot2.setSize(170, 100);
            txtaA2.requestFocus();
        } else {
            txpAAnot2.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA2ActionPerformed

    /**
     * Método btnFotoA2ActionPerformed Acionado ao clicar no botão de foto do
     * item A2.
     *
     * Define o prefixo do nome da imagem como "A2 - " e o atribui à variável
     * nome_foto. Em seguida, exibe o painel jPanel1 e ajusta seu tamanho para
     * 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A2.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A2
     */
    private void btnFotoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA2ActionPerformed

        nome1 = "A2 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA2ActionPerformed

    /**
     * Método rbtA3ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A3 é selecionado.
     *
     * Define a variável situacao2 como "Não Conforme", indicando que o item A3
     * não está em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA3ActionPerformed

        situacao2 = "Não Conforme";
    }//GEN-LAST:event_rbtA3ActionPerformed

    /**
     * Método rbtA13ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A3 é selecionado.
     *
     * Define a variável situacao2 como "Conforme", indicando que o item A3 está
     * em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA13ActionPerformed

        situacao2 = "Conforme";
    }//GEN-LAST:event_rbtA13ActionPerformed

    /**
     * Método btnAnoA3ActionPerformed Acionado ao clicar no botão de anotação do
     * item A3.
     *
     * Exibe ou oculta o campo de anotação txpAAnot3 com base no estado do botão
     * btnAnoA3. Se selecionado, o campo é exibido com tamanho 170x100 pixels;
     * caso contrário, é ocultado.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A3
     */
    private void btnAnoA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA3ActionPerformed

        if (btnAnoA3.isSelected()) {
            txpAAnot3.setVisible(true);
            txpAAnot3.setSize(170, 100);
            txtaA3.requestFocus();
        } else {
            txpAAnot3.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA3ActionPerformed

    /**
     * Método btnFotoA3ActionPerformed Acionado ao clicar no botão de foto do
     * item A3.
     *
     * Define o prefixo do nome da imagem como "A3 - " e o atribui à variável
     * nome_foto. Em seguida, exibe o painel jPanel1 e ajusta seu tamanho para
     * 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A3.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A3
     */
    private void btnFotoA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA3ActionPerformed

        nome1 = "A3 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA3ActionPerformed

    /**
     * Método rbtA4ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A4 é selecionado.
     *
     * Define a variável situacao3 como "Não Conforme", indicando que o item A4
     * não está em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA4ActionPerformed

        situacao3 = "Não Conforme";
    }//GEN-LAST:event_rbtA4ActionPerformed

    /**
     * Método rbtA14ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A4 é selecionado.
     *
     * Define a variável situacao3 como "Conforme", indicando que o item A4 está
     * em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA14ActionPerformed

        situacao3 = "Conforme";
    }//GEN-LAST:event_rbtA14ActionPerformed

    /**
     * Método btnAnoA4ActionPerformed Acionado ao clicar no botão de anotação do
     * item A4.
     *
     * Exibe ou oculta o campo de anotação txpAAnot4 com base no estado do botão
     * btnAnoA4. Se selecionado, o campo é exibido com tamanho 170x100 pixels;
     * caso contrário, é ocultado.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A4
     */
    private void btnAnoA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA4ActionPerformed

        if (btnAnoA4.isSelected()) {
            txpAAnot4.setVisible(true);
            txpAAnot4.setSize(170, 100);
            txtaA4.requestFocus();
        } else {
            txpAAnot4.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA4ActionPerformed

    /**
     * Método btnFotoA4ActionPerformed Acionado ao clicar no botão de foto do
     * item A4.
     *
     * Define o prefixo do nome da imagem como "A4 - " e o atribui à variável
     * nome_foto. Em seguida, exibe o painel jPanel1 e ajusta seu tamanho para
     * 270x250 pixels.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A4
     */
    private void btnFotoA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA4ActionPerformed

        nome1 = "A4 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA4ActionPerformed

    /**
     * Método rbtA5ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A5 é selecionado.
     *
     * Define a variável situacao4 como "Não Conforme", indicando que o item A5
     * não está em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA5ActionPerformed

        situacao4 = "Não Conforme";
    }//GEN-LAST:event_rbtA5ActionPerformed

    /**
     * Método rbtA15ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A5 é selecionado.
     *
     * Define a variável situacao4 como "Conforme", indicando que o item A5 está
     * em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA15ActionPerformed

        situacao4 = "Conforme";
    }//GEN-LAST:event_rbtA15ActionPerformed

    /**
     * Método btnAnoA5ActionPerformed Acionado ao clicar no botão de anotação do
     * item A5.
     *
     * Exibe ou oculta o campo de anotação txpAAnot5 com base no estado do botão
     * btnAnoA5.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A5
     */
    private void btnAnoA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA5ActionPerformed

        if (btnAnoA5.isSelected()) {
            txpAAnot5.setVisible(true);
            txpAAnot5.setSize(170, 100);
            txtaA5.requestFocus();
        } else {
            txpAAnot5.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA5ActionPerformed

    /**
     * Método btnFotoA5ActionPerformed Acionado ao clicar no botão de foto do
     * item A5.
     *
     * Define o prefixo do nome da imagem como "A5 - " e o atribui à variável
     * nome_foto. Exibe o painel jPanel1 para visualização da imagem.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A5
     */
    private void btnFotoA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA5ActionPerformed

        nome1 = "A5 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA5ActionPerformed

    /**
     * Método rbtA6ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A6 é selecionado.
     *
     * Define a variável situacao5 como "Não Conforme", indicando que o item A6
     * não está em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA6ActionPerformed

        situacao5 = "Não Conforme";
    }//GEN-LAST:event_rbtA6ActionPerformed

    /**
     * Método rbtA16ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A6 é selecionado.
     *
     * Define a variável situacao5 como "Conforme", indicando que o item A6 está
     * em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA16ActionPerformed

        situacao5 = "Conforme";
    }//GEN-LAST:event_rbtA16ActionPerformed

    /**
     * Método btnAnoA6ActionPerformed Acionado ao clicar no botão de anotação do
     * item A6.
     *
     * Exibe ou oculta o campo de anotação txpAAnot6 com base no estado do botão
     * btnAnoA6.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A6
     */
    private void btnAnoA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA6ActionPerformed

        if (btnAnoA6.isSelected()) {
            txpAAnot6.setVisible(true);
            txpAAnot6.setSize(170, 100);
            txtaA6.requestFocus();
        } else {
            txpAAnot6.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA6ActionPerformed

    /**
     * Método btnFotoA6ActionPerformed Acionado ao clicar no botão de foto do
     * item A6.
     *
     * Define o prefixo do nome da imagem como "A6 - " e o atribui à variável
     * nome_foto. Exibe o painel jPanel1 para visualização da imagem.
     *
     * @param evt o evento de ação gerado pelo clique no botão de foto A6
     */
    private void btnFotoA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA6ActionPerformed

        nome1 = "A6 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA6ActionPerformed

    /**
     * Método rbtA7ActionPerformed Acionado quando o botão de rádio "Não
     * Conforme" do item A7 é selecionado.
     *
     * Define a variável situacao6 como "Não Conforme", indicando que o item A7
     * não está em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA7ActionPerformed

        situacao6 = "Não Conforme";
    }//GEN-LAST:event_rbtA7ActionPerformed

    /**
     * Método rbtA17ActionPerformed Acionado quando o botão de rádio "Conforme"
     * do item A7 é selecionado.
     *
     * Define a variável situacao6 como "Conforme", indicando que o item A7 está
     * em conformidade.
     *
     * @param evt o evento de ação gerado pela seleção do botão de rádio
     */
    private void rbtA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA17ActionPerformed

        situacao6 = "Conforme";
    }//GEN-LAST:event_rbtA17ActionPerformed

    /**
     * Método btnAnoA7ActionPerformed Acionado ao clicar no botão de anotação do
     * item A7.
     *
     * Exibe ou oculta o campo de anotação txpAAnot7 com base no estado do botão
     * btnAnoA7.
     *
     * @param evt o evento de ação gerado pela interação com o botão de anotação
     * A7
     */
    private void btnAnoA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA7ActionPerformed

        if (btnAnoA7.isSelected()) {
            txpAAnot7.setVisible(true);
            txpAAnot7.setSize(170, 100);
            txtaA7.requestFocus();
        } else {
            txpAAnot7.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA7ActionPerformed

    /**
     * Método btnFotoA7ActionPerformed Acionado ao clicar no botão de foto do
     * item A7.
     *
     * Define o nome da imagem como "A7 - ", exibe o painel jPanel1 e configura
     * seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A7
     */
    private void btnFotoA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA7ActionPerformed

        nome1 = "A7 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA7ActionPerformed

    /**
     * Método rbtA8ActionPerformed Define a variável situacao7 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A8.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A8
     */
    private void rbtA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA8ActionPerformed

        situacao7 = "Não Conforme";
    }//GEN-LAST:event_rbtA8ActionPerformed

    /**
     * Método rbtA18ActionPerformed Define a variável situacao7 como "Conforme"
     * ao selecionar o botão correspondente ao item A8.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A8
     */
    private void rbtA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA18ActionPerformed

        situacao7 = "Conforme";
    }//GEN-LAST:event_rbtA18ActionPerformed

    /**
     * Método btnAnoA8ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot8 com base no estado do botão btnAnoA8.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A8
     */
    private void btnAnoA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA8ActionPerformed

        if (btnAnoA8.isSelected()) {
            txpAAnot8.setVisible(true);
            txpAAnot8.setSize(170, 100);
            txtaA8.requestFocus();
        } else {
            txpAAnot8.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA8ActionPerformed

    /**
     * Método btnFotoA8ActionPerformed Define o nome da imagem como "A8 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A8
     */
    private void btnFotoA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA8ActionPerformed

        nome1 = "A8 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA8ActionPerformed

    /**
     * Método rbtA9ActionPerformed Define a variável situacao8 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A9.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A9
     */
    private void rbtA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA9ActionPerformed

        situacao8 = "Não Conforme";
    }//GEN-LAST:event_rbtA9ActionPerformed

    /**
     * Método rbtA19ActionPerformed Define a variável situacao8 como "Conforme"
     * ao selecionar o botão correspondente ao item A9.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A9
     */
    private void rbtA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA19ActionPerformed

        situacao8 = "Conforme";
    }//GEN-LAST:event_rbtA19ActionPerformed

    /**
     * Método btnAnoA9ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot9 com base no estado do botão btnAnoA9.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A9
     */
    private void btnAnoA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA9ActionPerformed

        if (btnAnoA9.isSelected()) {
            txpAAnot9.setVisible(true);
            txpAAnot9.setSize(170, 100);
            txtaA9.requestFocus();
        } else {
            txpAAnot9.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA9ActionPerformed

    /**
     * Método btnFotoA9ActionPerformed Define o nome da imagem como "A9 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A9
     */
    private void btnFotoA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA9ActionPerformed

        nome1 = "A9 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA9ActionPerformed

    /**
     * Método rbtA10ActionPerformed Define a variável situacao9 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A10.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A10
     */
    private void rbtA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA10ActionPerformed

        situacao9 = "Não Conforme";
    }//GEN-LAST:event_rbtA10ActionPerformed

    /**
     * Método rbtA20ActionPerformed Define a variável situacao9 como "Conforme"
     * ao selecionar o botão correspondente ao item A10.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A10
     */
    private void rbtA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA20ActionPerformed

        situacao9 = "Conforme";
    }//GEN-LAST:event_rbtA20ActionPerformed

    /**
     * Método btnAnoA10ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot10 com base no estado do botão btnAnoA10.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A10
     */
    private void btnAnoA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA10ActionPerformed

        if (btnAnoA10.isSelected()) {
            txpAAnot10.setVisible(true);
            txpAAnot10.setSize(170, 100);
            txtaA10.requestFocus();
        } else {
            txpAAnot10.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA10ActionPerformed

    /**
     * Método btnFotoA10ActionPerformed Define o nome da imagem como "A10 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A10
     */
    private void btnFotoA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA10ActionPerformed

        nome1 = "A10 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA10ActionPerformed

    /**
     * Método rbtA21ActionPerformed Define a variável situacao10 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A11.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A11
     */
    private void rbtA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA21ActionPerformed

        situacao10 = "Não Conforme";
    }//GEN-LAST:event_rbtA21ActionPerformed

    /**
     * Método rbtA31ActionPerformed Define a variável situacao10 como "Conforme"
     * ao selecionar o botão correspondente ao item A11.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A11
     */
    private void rbtA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA31ActionPerformed

        situacao10 = "Conforme";
    }//GEN-LAST:event_rbtA31ActionPerformed

    /**
     * Método btnAnoA11ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot11 com base no estado do botão btnAnoA11.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A11
     */
    private void btnAnoA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA11ActionPerformed

        if (btnAnoA11.isSelected()) {
            txpAAnot11.setVisible(true);
            txpAAnot11.setSize(170, 100);
            txtaA11.requestFocus();
        } else {
            txpAAnot11.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA11ActionPerformed

    /**
     * Método btnFotoA11ActionPerformed Define o nome da imagem como "A11 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A11
     */
    private void btnFotoA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA11ActionPerformed

        nome1 = "A11 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA11ActionPerformed

    /**
     * Método rbtA22ActionPerformed Define a variável situacao11 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A12.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A12
     */
    private void rbtA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA22ActionPerformed

        situacao11 = "Não Conforme";
    }//GEN-LAST:event_rbtA22ActionPerformed

    /**
     * Método rbtA32ActionPerformed Define a variável situacao11 como "Conforme"
     * ao selecionar o botão correspondente ao item A12.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A12
     */
    private void rbtA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA32ActionPerformed

        situacao11 = "Conforme";
    }//GEN-LAST:event_rbtA32ActionPerformed

    /**
     * Método btnAnoA12ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot12 com base no estado do botão btnAnoA12.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A12
     */
    private void btnAnoA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA12ActionPerformed

        if (btnAnoA12.isSelected()) {
            txpAAnot12.setVisible(true);
            txpAAnot12.setSize(170, 100);
            txtaA12.requestFocus();
        } else {
            txpAAnot12.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA12ActionPerformed

    /**
     * Método btnFotoA12ActionPerformed Define o nome da imagem como "A12 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A12
     */
    private void btnFotoA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA12ActionPerformed

        nome1 = "A12 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA12ActionPerformed

    /**
     * Método rbtA23ActionPerformed Define a variável situacao12 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A13.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A13
     */
    private void rbtA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA23ActionPerformed

        situacao12 = "Não Conforme";
    }//GEN-LAST:event_rbtA23ActionPerformed

    /**
     * Método rbtA33ActionPerformed Define a variável situacao12 como "Conforme"
     * ao selecionar o botão correspondente ao item A13.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A13
     */
    private void rbtA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA33ActionPerformed

        situacao12 = "Conforme";
    }//GEN-LAST:event_rbtA33ActionPerformed

    /**
     * Método btnAnoA13ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot13 com base no estado do botão btnAnoA13.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A13
     */
    private void btnAnoA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA13ActionPerformed

        if (btnAnoA13.isSelected()) {
            txpAAnot13.setVisible(true);
            txpAAnot13.setSize(170, 100);
            txtaA13.requestFocus();
        } else {
            txpAAnot13.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA13ActionPerformed

    /**
     * Método btnFotoA13ActionPerformed Define o nome da imagem como "A13 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A13
     */
    private void btnFotoA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA13ActionPerformed

        nome1 = "A13 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA13ActionPerformed

    /**
     * Método rbtA24ActionPerformed Define a variável situacao13 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A14.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A14
     */
    private void rbtA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA24ActionPerformed

        situacao13 = "Não Conforme";
    }//GEN-LAST:event_rbtA24ActionPerformed

    /**
     * Método rbtA34ActionPerformed Define a variável situacao13 como "Conforme"
     * ao selecionar o botão correspondente ao item A14.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A14
     */
    private void rbtA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA34ActionPerformed

        situacao13 = "Conforme";
    }//GEN-LAST:event_rbtA34ActionPerformed

    /**
     * Método btnAnoA14ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot14 com base no estado do botão btnAnoA14.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A14
     */
    private void btnAnoA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA14ActionPerformed

        if (btnAnoA14.isSelected()) {
            txpAAnot14.setVisible(true);
            txpAAnot14.setSize(170, 100);
            txtaA14.requestFocus();
        } else {
            txpAAnot14.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA14ActionPerformed

    /**
     * Método btnFotoA14ActionPerformed Define o nome da imagem como "A14 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A14
     */
    private void btnFotoA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA14ActionPerformed

        nome1 = "A14 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA14ActionPerformed

    /**
     * Método rbtA25ActionPerformed Define a variável situacao14 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A15.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A15
     */
    private void rbtA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA25ActionPerformed

        situacao14 = "Não Conforme";
    }//GEN-LAST:event_rbtA25ActionPerformed

    /**
     * Método rbtA35ActionPerformed Define a variável situacao14 como "Conforme"
     * ao selecionar o botão correspondente ao item A15.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A15
     */
    private void rbtA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA35ActionPerformed

        situacao14 = "Conforme";
    }//GEN-LAST:event_rbtA35ActionPerformed

    /**
     * Método btnAnoA15ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot15 com base no estado do botão btnAnoA15.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A15
     */
    private void btnAnoA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA15ActionPerformed

        if (btnAnoA15.isSelected()) {
            txpAAnot15.setVisible(true);
            txpAAnot15.setSize(170, 100);
            txtaA15.requestFocus();
        } else {
            txpAAnot15.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA15ActionPerformed

    /**
     * Método btnFotoA15ActionPerformed Define o nome da imagem como "A15 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A15
     */
    private void btnFotoA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA15ActionPerformed

        nome1 = "A15 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA15ActionPerformed

    /**
     * Método rbtA26ActionPerformed Define a variável situacao15 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A16.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A16
     */
    private void rbtA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA26ActionPerformed

        situacao15 = "Não Conforme";
    }//GEN-LAST:event_rbtA26ActionPerformed

    /**
     * Método rbtA36ActionPerformed Define a variável situacao15 como "Conforme"
     * ao selecionar o botão correspondente ao item A16.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A16
     */
    private void rbtA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA36ActionPerformed

        situacao15 = "Conforme";
    }//GEN-LAST:event_rbtA36ActionPerformed

    /**
     * Método btnAnoA16ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot16 com base no estado do botão btnAnoA16.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A16
     */
    private void btnAnoA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA16ActionPerformed

        if (btnAnoA16.isSelected()) {
            txpAAnot16.setVisible(true);
            txpAAnot16.setSize(170, 100);
            txtaA16.requestFocus();
        } else {
            txpAAnot16.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA16ActionPerformed

    /**
     * Método btnFotoA16ActionPerformed Define o nome da imagem como "A16 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A16
     */
    private void btnFotoA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA16ActionPerformed

        nome1 = "A16 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA16ActionPerformed

    /**
     * Método rbtA27ActionPerformed Define a variável situacao16 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A17.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A17
     */
    private void rbtA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA27ActionPerformed

        situacao16 = "Não Conforme";
    }//GEN-LAST:event_rbtA27ActionPerformed

    /**
     * Método rbtA37ActionPerformed Define a variável situacao16 como "Conforme"
     * ao selecionar o botão correspondente ao item A17.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A17
     */
    private void rbtA37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA37ActionPerformed

        situacao16 = "Conforme";
    }//GEN-LAST:event_rbtA37ActionPerformed

    /**
     * Método btnAnoA17ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot17 com base no estado do botão btnAnoA17.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A17
     */
    private void btnAnoA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA17ActionPerformed

        if (btnAnoA17.isSelected()) {
            txpAAnot17.setVisible(true);
            txpAAnot17.setSize(170, 100);
            txtaA17.requestFocus();
        } else {
            txpAAnot17.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA17ActionPerformed

    /**
     * Método btnFotoA17ActionPerformed Define o nome da imagem como "A17 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A17
     */
    private void btnFotoA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA17ActionPerformed

        nome1 = "A17 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA17ActionPerformed

    /**
     * Método rbtA28ActionPerformed Define a variável situacao17 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A18.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A18
     */
    private void rbtA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA28ActionPerformed

        situacao17 = "Não Conforme";
    }//GEN-LAST:event_rbtA28ActionPerformed

    /**
     * Método rbtA38ActionPerformed Define a variável situacao17 como "Conforme"
     * ao selecionar o botão correspondente ao item A18.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A18
     */
    private void rbtA38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA38ActionPerformed

        situacao17 = "Conforme";
    }//GEN-LAST:event_rbtA38ActionPerformed

    /**
     * Método btnAnoA18ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot18 com base no estado do botão btnAnoA18.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A18
     */
    private void btnAnoA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA18ActionPerformed

        if (btnAnoA18.isSelected()) {
            txpAAnot18.setVisible(true);
            txpAAnot18.setSize(170, 100);
            txtaA18.requestFocus();
        } else {
            txpAAnot18.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA18ActionPerformed

    /**
     * Método btnFotoA18ActionPerformed Define o nome da imagem como "A18 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A18
     */
    private void btnFotoA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA18ActionPerformed

        nome1 = "A18 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA18ActionPerformed

    /**
     * Método rbtA29ActionPerformed Define a variável situacao18 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A19.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A19
     */
    private void rbtA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA29ActionPerformed

        situacao18 = "Não Conforme";
    }//GEN-LAST:event_rbtA29ActionPerformed

    /**
     * Método rbtA39ActionPerformed Define a variável situacao18 como "Conforme"
     * ao selecionar o botão correspondente ao item A19.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A19
     */
    private void rbtA39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA39ActionPerformed

        situacao18 = "Conforme";
    }//GEN-LAST:event_rbtA39ActionPerformed

    /**
     * Método btnAnoA19ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot19 com base no estado do botão btnAnoA19.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A19
     */
    private void btnAnoA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA19ActionPerformed

        if (btnAnoA19.isSelected()) {
            txpAAnot19.setVisible(true);
            txpAAnot19.setSize(170, 100);
            txtaA19.requestFocus();
        } else {
            txpAAnot19.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA19ActionPerformed

    /**
     * Método btnFotoA19ActionPerformed Define o nome da imagem como "A19 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A19
     */
    private void btnFotoA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA19ActionPerformed

        nome1 = "A19 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA19ActionPerformed

    /**
     * Método rbtA30ActionPerformed Define a variável situacao19 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A20.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A20
     */
    private void rbtA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA30ActionPerformed

        situacao19 = "Não Conforme";
    }//GEN-LAST:event_rbtA30ActionPerformed

    /**
     * Método rbtA40ActionPerformed Define a variável situacao19 como "Conforme"
     * ao selecionar o botão correspondente ao item A20.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A20
     */
    private void rbtA40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA40ActionPerformed

        situacao19 = "Conforme";
    }//GEN-LAST:event_rbtA40ActionPerformed

    /**
     * Método btnAnoA20ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot20 com base no estado do botão btnAnoA20.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A20
     */
    private void btnAnoA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA20ActionPerformed

        if (btnAnoA20.isSelected()) {
            txpAAnot20.setVisible(true);
            txpAAnot20.setSize(170, 100);
            txtaA20.requestFocus();
        } else {
            txpAAnot20.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA20ActionPerformed

    /**
     * Método btnFotoA20ActionPerformed Define o nome da imagem como "A20 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A20
     */
    private void btnFotoA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA20ActionPerformed

        nome1 = "A20 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA20ActionPerformed

    /**
     * Método rbtA41ActionPerformed Define a variável situacao20 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A21.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A21
     */
    private void rbtA41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA41ActionPerformed

        situacao20 = "Não Conforme";
    }//GEN-LAST:event_rbtA41ActionPerformed

    /**
     * Método rbtA51ActionPerformed Define a variável situacao20 como "Conforme"
     * ao selecionar o botão correspondente ao item A21.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A21
     */
    private void rbtA51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA51ActionPerformed

        situacao20 = "Conforme";
    }//GEN-LAST:event_rbtA51ActionPerformed

    /**
     * Método btnAnoA21ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot21 com base no estado do botão btnAnoA21.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A21
     */
    private void btnAnoA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA21ActionPerformed

        if (btnAnoA21.isSelected()) {
            txpAAnot21.setVisible(true);
            txpAAnot21.setSize(170, 100);
            txtaA21.requestFocus();
        } else {
            txpAAnot21.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA21ActionPerformed

    /**
     * Método btnFotoA21ActionPerformed Define o nome da imagem como "A21 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A21
     */
    private void btnFotoA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA21ActionPerformed

        nome1 = "A21 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA21ActionPerformed

    /**
     * Método rbtA42ActionPerformed Define a variável situacao21 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A22.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A22
     */
    private void rbtA42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA42ActionPerformed

        situacao21 = "Não Conforme";
    }//GEN-LAST:event_rbtA42ActionPerformed

    /**
     * Método rbtA52ActionPerformed Define a variável situacao21 como "Conforme"
     * ao selecionar o botão correspondente ao item A22.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A22
     */
    private void rbtA52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA52ActionPerformed

        situacao21 = "Conforme";
    }//GEN-LAST:event_rbtA52ActionPerformed

    /**
     * Método btnAnoA22ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot22 com base no estado do botão btnAnoA22.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A22
     */
    private void btnAnoA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA22ActionPerformed

        if (btnAnoA22.isSelected()) {
            txpAAnot22.setVisible(true);
            txpAAnot22.setSize(170, 100);
            txtaA22.requestFocus();
        } else {
            txpAAnot22.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA22ActionPerformed

    /**
     * Método btnFotoA22ActionPerformed Define o nome da imagem como "A22 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A22
     */
    private void btnFotoA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA22ActionPerformed

        nome1 = "A22 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA22ActionPerformed

    /**
     * Método rbtA43ActionPerformed Define a variável situacao22 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A23.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A23
     */
    private void rbtA43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA43ActionPerformed

        situacao22 = "Não Conforme";
    }//GEN-LAST:event_rbtA43ActionPerformed

    /**
     * Método rbtA53ActionPerformed Define a variável situacao22 como "Conforme"
     * ao selecionar o botão correspondente ao item A23.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A23
     */
    private void rbtA53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA53ActionPerformed

        situacao22 = "Conforme";
    }//GEN-LAST:event_rbtA53ActionPerformed

    /**
     * Método btnAnoA23ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot23 com base no estado do botão btnAnoA23.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A23
     */
    private void btnAnoA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA23ActionPerformed

        if (btnAnoA23.isSelected()) {
            txpAAnot23.setVisible(true);
            txpAAnot23.setSize(170, 100);
            txtaA23.requestFocus();
        } else {
            txpAAnot23.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA23ActionPerformed

    /**
     * Método btnFotoA23ActionPerformed Define o nome da imagem como "A23 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A23
     */
    private void btnFotoA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA23ActionPerformed

        nome1 = "A23 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA23ActionPerformed

    /**
     * Método rbtA44ActionPerformed Define a variável situacao23 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A24.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A24
     */
    private void rbtA44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA44ActionPerformed

        situacao23 = "Não Conforme";
    }//GEN-LAST:event_rbtA44ActionPerformed

    /**
     * Método rbtA54ActionPerformed Define a variável situacao23 como "Conforme"
     * ao selecionar o botão correspondente ao item A24.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A24
     */
    private void rbtA54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA54ActionPerformed

        situacao23 = "Conforme";
    }//GEN-LAST:event_rbtA54ActionPerformed

    /**
     * Método btnAnoA24ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot24 com base no estado do botão btnAnoA24.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A24
     */
    private void btnAnoA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA24ActionPerformed

        if (btnAnoA24.isSelected()) {
            txpAAnot24.setVisible(true);
            txpAAnot24.setSize(170, 100);
            txtaA24.requestFocus();
        } else {
            txpAAnot24.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA24ActionPerformed

    /**
     * Método btnFotoA24ActionPerformed Define o nome da imagem como "A24 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A24
     */
    private void btnFotoA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA24ActionPerformed

        nome1 = "A24 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA24ActionPerformed

    /**
     * Método rbtA45ActionPerformed Define a variável situacao24 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A25.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A25
     */
    private void rbtA45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA45ActionPerformed

        situacao24 = "Não Conforme";
    }//GEN-LAST:event_rbtA45ActionPerformed

    /**
     * Método rbtA55ActionPerformed Define a variável situacao24 como "Conforme"
     * ao selecionar o botão correspondente ao item A25.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A25
     */
    private void rbtA55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA55ActionPerformed

        situacao24 = "Conforme";
    }//GEN-LAST:event_rbtA55ActionPerformed

    /**
     * Método btnAnoA25ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot25 com base no estado do botão btnAnoA25.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A25
     */
    private void btnAnoA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA25ActionPerformed

        if (btnAnoA25.isSelected()) {
            txpAAnot25.setVisible(true);
            txpAAnot25.setSize(170, 100);
            txtaA25.requestFocus();
        } else {
            txpAAnot25.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA25ActionPerformed

    /**
     * Método btnFotoA25ActionPerformed Define o nome da imagem como "A25 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A25
     */
    private void btnFotoA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA25ActionPerformed

        nome1 = "A25 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA25ActionPerformed

    /**
     * Método rbtA46ActionPerformed Define a variável situacao25 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A26.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A26
     */
    private void rbtA46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA46ActionPerformed

        situacao25 = "Não Conforme";
    }//GEN-LAST:event_rbtA46ActionPerformed

    /**
     * Método rbtA56ActionPerformed Define a variável situacao25 como "Conforme"
     * ao selecionar o botão correspondente ao item A26.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A26
     */
    private void rbtA56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA56ActionPerformed

        situacao25 = "Conforme";
    }//GEN-LAST:event_rbtA56ActionPerformed

    /**
     * Método btnAnoA26ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot26 com base no estado do botão btnAnoA26.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A26
     */
    private void btnAnoA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA26ActionPerformed

        if (btnAnoA26.isSelected()) {
            txpAAnot26.setVisible(true);
            txpAAnot26.setSize(170, 100);
            txtaA26.requestFocus();
        } else {
            txpAAnot26.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA26ActionPerformed

    /**
     * Método btnFotoA26ActionPerformed Define o nome da imagem como "A26 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A26
     */
    private void btnFotoA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA26ActionPerformed

        nome1 = "A26 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA26ActionPerformed

    /**
     * Método rbtA47ActionPerformed Define a variável situacao26 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A27.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A27
     */
    private void rbtA47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA47ActionPerformed

        situacao26 = "Não Conforme";
    }//GEN-LAST:event_rbtA47ActionPerformed

    /**
     * Método btnAnoA27ActionPerformed Acionado ao clicar no botão de anotação
     * do item A27.
     *
     * Exibe ou oculta a área de anotação txpAAnot27 com base no estado do botão
     * btnAnoA27. Se estiver selecionado, o campo é exibido com tamanho 170x100
     * pixels; caso contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A27
     */
    private void btnAnoA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA27ActionPerformed

        if (btnAnoA27.isSelected()) {
            txpAAnot27.setVisible(true);
            txpAAnot27.setSize(170, 100);
            txtaA27.requestFocus();
        } else {
            txpAAnot27.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA27ActionPerformed

    /**
     * Método btnFotoA27ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A27. Define o nome da imagem
     * como "A27 - ", exibe o painel jPanel1 e ajusta seu tamanho para permitir
     * a visualização ou captura da imagem correspondente ao item A27.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A27
     */
    private void btnFotoA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA27ActionPerformed

        nome1 = "A27 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA27ActionPerformed

    /**
     * Método rbtA48ActionPerformed Define a variável situacao27 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A28.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A28
     */
    private void rbtA48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA48ActionPerformed

        situacao27 = "Não Conforme";
    }//GEN-LAST:event_rbtA48ActionPerformed

    /**
     * Método rbtA58ActionPerformed Define a variável situacao27 como "Conforme"
     * ao selecionar o botão correspondente ao item A28.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A28
     */
    private void rbtA58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA58ActionPerformed

        situacao27 = "Conforme";
    }//GEN-LAST:event_rbtA58ActionPerformed

    /**
     * Método btnAnoA28ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot28 com base no estado do botão btnAnoA28.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A28
     */
    private void btnAnoA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA28ActionPerformed

        if (btnAnoA28.isSelected()) {
            txpAAnot28.setVisible(true);
            txpAAnot28.setSize(170, 100);
            txtaA28.requestFocus();
        } else {
            txpAAnot28.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA28ActionPerformed

    /**
     * Método btnFotoA28ActionPerformed Define o nome da imagem como "A28 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A28
     */
    private void btnFotoA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA28ActionPerformed

        nome1 = "A28 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA28ActionPerformed

    /**
     * Método rbtA49ActionPerformed Define a variável situacao28 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A29.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A29
     */
    private void rbtA49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA49ActionPerformed

        situacao28 = "Não Conforme";
    }//GEN-LAST:event_rbtA49ActionPerformed

    /**
     * Método rbtA59ActionPerformed Define a variável situacao28 como "Conforme"
     * ao selecionar o botão correspondente ao item A29.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A29
     */
    private void rbtA59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA59ActionPerformed

        situacao28 = "Conforme";
    }//GEN-LAST:event_rbtA59ActionPerformed

    /**
     * Método btnAnoA29ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot29 com base no estado do botão btnAnoA29.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A29
     */
    private void btnAnoA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA29ActionPerformed

        if (btnAnoA29.isSelected()) {
            txpAAnot29.setVisible(true);
            txpAAnot29.setSize(170, 100);
            txtaA29.requestFocus();
        } else {
            txpAAnot29.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA29ActionPerformed

    /**
     * Método btnFotoA29ActionPerformed Define o nome da imagem como "A29 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A29
     */
    private void btnFotoA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA29ActionPerformed

        nome1 = "A29 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA29ActionPerformed

    /**
     * Método rbtA50ActionPerformed Define a variável situacao29 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A30.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A30
     */
    private void rbtA50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA50ActionPerformed

        situacao29 = "Não Conforme";
    }//GEN-LAST:event_rbtA50ActionPerformed

    /**
     * Método rbtA60ActionPerformed Define a variável situacao29 como "Conforme"
     * ao selecionar o botão correspondente ao item A30.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A30
     */
    private void rbtA60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA60ActionPerformed

        situacao29 = "Conforme";
    }//GEN-LAST:event_rbtA60ActionPerformed

    /**
     * Método btnAnoA30ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot30 com base no estado do botão btnAnoA30.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A30
     */
    private void btnAnoA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA30ActionPerformed

        if (btnAnoA30.isSelected()) {
            txpAAnot30.setVisible(true);
            txpAAnot30.setSize(170, 100);
            txtaA30.requestFocus();
        } else {
            txpAAnot30.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA30ActionPerformed

    /**
     * Método btnFotoA30ActionPerformed Define o nome da imagem como "A30 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A30
     */
    private void btnFotoA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA30ActionPerformed

        nome1 = "A30 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA30ActionPerformed

    /**
     * Método rbtA61ActionPerformed Define a variável situacao30 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A31.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A31
     */
    private void rbtA61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA61ActionPerformed

        situacao30 = "Não Conforme";
    }//GEN-LAST:event_rbtA61ActionPerformed

    /**
     * Método rbtA71ActionPerformed Define a variável situacao30 como "Conforme"
     * ao selecionar o botão correspondente ao item A31.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A31
     */
    private void rbtA71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA71ActionPerformed

        situacao30 = "Conforme";
    }//GEN-LAST:event_rbtA71ActionPerformed

    /**
     * Método btnAnoA31ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot31 com base no estado do botão btnAnoA31.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A31
     */
    private void btnAnoA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA31ActionPerformed

        if (btnAnoA31.isSelected()) {
            txpAAnot31.setVisible(true);
            txpAAnot31.setSize(170, 100);
            txtaA31.requestFocus();
        } else {
            txpAAnot31.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA31ActionPerformed

    /**
     * Método btnFotoA31ActionPerformed Define o nome da imagem como "A31 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A31
     */
    private void btnFotoA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA31ActionPerformed

        nome1 = "A31 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA31ActionPerformed

    /**
     * Método rbtA62ActionPerformed Define a variável situacao31 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A32.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A32
     */
    private void rbtA62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA62ActionPerformed

        situacao31 = "Não Conforme";
    }//GEN-LAST:event_rbtA62ActionPerformed

    /**
     * Método rbtA72ActionPerformed Define a variável situacao31 como "Conforme"
     * ao selecionar o botão correspondente ao item A32.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A32
     */
    private void rbtA72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA72ActionPerformed

        situacao31 = "Conforme";
    }//GEN-LAST:event_rbtA72ActionPerformed

    /**
     * Método btnAnoA32ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot32 com base no estado do botão btnAnoA32.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A32
     */
    private void btnAnoA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA32ActionPerformed

        if (btnAnoA32.isSelected()) {
            txpAAnot32.setVisible(true);
            txpAAnot32.setSize(170, 100);
            txtaA32.requestFocus();
        } else {
            txpAAnot32.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA32ActionPerformed

    /**
     * Método btnFotoA32ActionPerformed Define o nome da imagem como "A32 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A32
     */
    private void btnFotoA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA32ActionPerformed

        nome1 = "A32 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA32ActionPerformed

    /**
     * Método formInternalFrameClosing Acionado quando o usuário tenta fechar a
     * janela interna.
     *
     * Exibe uma caixa de diálogo de confirmação. Se o usuário confirmar a
     * saída: - Chama o método iniciar_prev para realizar procedimentos de
     * encerramento - Fecha a janela com dispose()
     *
     * @param evt o evento de fechamento da janela interna
     */
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        int sair = JOptionPane.showConfirmDialog(null, "Tem Certeza Que Deseja Sair?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            iniciar_prev();
            dispose();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    /**
     * Método btnCamPreActionPerformed Acionado ao clicar no botão de captura de
     * imagem via webcam.
     *
     * Abre a tela TelaWebCam1 para captura de imagem e define o tipo de
     * preventiva como "Refrigeração - Semestral".
     *
     * @param evt o evento de ação gerado pelo clique no botão de câmera
     */
    private void btnCamPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCamPreActionPerformed

        TelaWebCam1 captura1 = new TelaWebCam1();
        captura1.setVisible(true);
        tipo_prev = "Refrigeração - Semestral";
    }//GEN-LAST:event_btnCamPreActionPerformed

    /**
     * Método txtCanFotActionPerformed Acionado ao clicar no botão de cancelar
     * foto.
     *
     * Oculta o painel de visualização da imagem, limpa o ícone exibido, apaga o
     * texto do campo txtFotPre11 e desabilita o botão de salvar foto.
     *
     * @param evt o evento de ação gerado pelo clique no botão de cancelar foto
     */
    private void txtCanFotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanFotActionPerformed

        jPanel1.setVisible(false);
        lblFotEltMen11.setIcon(null);
        txtFotPre11.setText(null);
        btnSalFotPre11.setEnabled(false);
    }//GEN-LAST:event_txtCanFotActionPerformed

    /**
     * Método btnArqFotoPreActionPerformed Acionado ao clicar no botão de
     * selecionar arquivo de foto.
     *
     * Chama o método select_arq para abrir o seletor de arquivos e define o
     * nome da foto com base na variável nome1.
     *
     * @param evt o evento de ação gerado pelo clique no botão de selecionar
     * arquivo
     */
    private void btnArqFotoPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArqFotoPreActionPerformed

        select_arq();
        nome_foto = nome1;
    }//GEN-LAST:event_btnArqFotoPreActionPerformed

    /**
     * Método btnSalFotPre11ActionPerformed Acionado ao clicar no botão de
     * salvar foto.
     *
     * Executa o método foto_prev para salvar a imagem capturada ou selecionada.
     *
     * @param evt o evento de ação gerado pelo clique no botão de salvar foto
     */
    private void btnSalFotPre11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalFotPre11ActionPerformed

        foto_prev();
    }//GEN-LAST:event_btnSalFotPre11ActionPerformed

    /**
     * Método rbtA63ActionPerformed Define a variável situacao32 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A33.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A33
     */
    private void rbtA63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA63ActionPerformed

        situacao32 = "Não Conforme";
    }//GEN-LAST:event_rbtA63ActionPerformed

    /**
     * Método rbtA73ActionPerformed Define a variável situacao32 como "Conforme"
     * ao selecionar o botão correspondente ao item A33.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A33
     */
    private void rbtA73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA73ActionPerformed

        situacao32 = "Conforme";
    }//GEN-LAST:event_rbtA73ActionPerformed

    /**
     * Método btnAnoA33ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot33 com base no estado do botão btnAnoA33.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A33
     */
    private void btnAnoA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA33ActionPerformed

        if (btnAnoA33.isSelected()) {
            txpAAnot33.setVisible(true);
            txpAAnot33.setSize(170, 100);
            txtaA33.requestFocus();
        } else {
            txpAAnot33.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA33ActionPerformed

    /**
     * Método btnFotoA33ActionPerformed Define o nome da imagem como "A33 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A33
     */
    private void btnFotoA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA33ActionPerformed

        nome1 = "A33 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA33ActionPerformed

    /**
     * Método rbtA64ActionPerformed Define a variável situacao33 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A34.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A34
     */
    private void rbtA64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA64ActionPerformed

        situacao33 = "Não Conforme";
    }//GEN-LAST:event_rbtA64ActionPerformed

    /**
     * Método rbtA74ActionPerformed Define a variável situacao33 como "Conforme"
     * ao selecionar o botão correspondente ao item A34.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A34
     */
    private void rbtA74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA74ActionPerformed

        situacao33 = "Conforme";
    }//GEN-LAST:event_rbtA74ActionPerformed

    /**
     * Método btnAnoA34ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot34 com base no estado do botão btnAnoA34.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A34
     */
    private void btnAnoA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA34ActionPerformed

        if (btnAnoA34.isSelected()) {
            txpAAnot34.setVisible(true);
            txpAAnot34.setSize(170, 100);
            txtaA34.requestFocus();
        } else {
            txpAAnot34.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA34ActionPerformed

    /**
     * Método btnFotoA34ActionPerformed Define o nome da imagem como "A34 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A34
     */
    private void btnFotoA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA34ActionPerformed

        nome1 = "A34 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA34ActionPerformed

    /**
     * Método rbtA65ActionPerformed Define a variável situacao34 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A35.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A35
     */
    private void rbtA65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA65ActionPerformed

        situacao34 = "Não Conforme";
    }//GEN-LAST:event_rbtA65ActionPerformed

    /**
     * Método rbtA75ActionPerformed Define a variável situacao34 como "Conforme"
     * ao selecionar o botão correspondente ao item A35.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A35
     */
    private void rbtA75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA75ActionPerformed

        situacao34 = "Conforme";
    }//GEN-LAST:event_rbtA75ActionPerformed

    /**
     * Método btnAnoA35ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot35 com base no estado do botão btnAnoA35.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A35
     */
    private void btnAnoA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA35ActionPerformed

        if (btnAnoA35.isSelected()) {
            txpAAnot35.setVisible(true);
            txpAAnot35.setSize(170, 100);
            txtaA35.requestFocus();
        } else {
            txpAAnot35.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA35ActionPerformed

    /**
     * Método btnFotoA35ActionPerformed Define o nome da imagem como "A35 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A35
     */
    private void btnFotoA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA35ActionPerformed

        nome1 = "A35 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA35ActionPerformed

    /**
     * Método rbtA66ActionPerformed Define a variável situacao35 como "Não
     * Conforme" ao selecionar o botão correspondente ao item A36.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Não Conforme"
     * do item A36
     */
    private void rbtA66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA66ActionPerformed

        situacao35 = "Não Conforme";
    }//GEN-LAST:event_rbtA66ActionPerformed

    /**
     * Método rbtA76ActionPerformed Define a variável situacao35 como "Conforme"
     * ao selecionar o botão correspondente ao item A36.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A36
     */
    private void rbtA76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA76ActionPerformed

        situacao35 = "Conforme";
    }//GEN-LAST:event_rbtA76ActionPerformed

    /**
     * Método btnAnoA36ActionPerformed Exibe ou oculta a área de anotação
     * txpAAnot36 com base no estado do botão btnAnoA36.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A36
     */
    private void btnAnoA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA36ActionPerformed

        if (btnAnoA36.isSelected()) {
            txpAAnot36.setVisible(true);
            txpAAnot36.setSize(170, 100);
            txtaA36.requestFocus();
        } else {
            txpAAnot36.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA36ActionPerformed

    /**
     * Método btnFotoA36ActionPerformed Define o nome da imagem como "A36 - ",
     * exibe o painel jPanel1 e configura seu tamanho.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A36
     */
    private void btnFotoA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA36ActionPerformed

        nome1 = "A36 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA36ActionPerformed

    /**
     * Método rbtA57ActionPerformed Define a variável situacao26 como "Conforme"
     * ao selecionar o botão correspondente ao item A27.
     *
     * @param evt o evento gerado pela seleção do botão de rádio "Conforme" do
     * item A27
     */
    private void rbtA57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA57ActionPerformed

        situacao26 = "Conforme";
    }//GEN-LAST:event_rbtA57ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAnoA1;
    private javax.swing.JToggleButton btnAnoA10;
    private javax.swing.JToggleButton btnAnoA11;
    private javax.swing.JToggleButton btnAnoA12;
    private javax.swing.JToggleButton btnAnoA13;
    private javax.swing.JToggleButton btnAnoA14;
    private javax.swing.JToggleButton btnAnoA15;
    private javax.swing.JToggleButton btnAnoA16;
    private javax.swing.JToggleButton btnAnoA17;
    private javax.swing.JToggleButton btnAnoA18;
    private javax.swing.JToggleButton btnAnoA19;
    private javax.swing.JToggleButton btnAnoA2;
    private javax.swing.JToggleButton btnAnoA20;
    private javax.swing.JToggleButton btnAnoA21;
    private javax.swing.JToggleButton btnAnoA22;
    private javax.swing.JToggleButton btnAnoA23;
    private javax.swing.JToggleButton btnAnoA24;
    private javax.swing.JToggleButton btnAnoA25;
    private javax.swing.JToggleButton btnAnoA26;
    private javax.swing.JToggleButton btnAnoA27;
    private javax.swing.JToggleButton btnAnoA28;
    private javax.swing.JToggleButton btnAnoA29;
    private javax.swing.JToggleButton btnAnoA3;
    private javax.swing.JToggleButton btnAnoA30;
    private javax.swing.JToggleButton btnAnoA31;
    private javax.swing.JToggleButton btnAnoA32;
    private javax.swing.JToggleButton btnAnoA33;
    private javax.swing.JToggleButton btnAnoA34;
    private javax.swing.JToggleButton btnAnoA35;
    private javax.swing.JToggleButton btnAnoA36;
    private javax.swing.JToggleButton btnAnoA4;
    private javax.swing.JToggleButton btnAnoA5;
    private javax.swing.JToggleButton btnAnoA6;
    private javax.swing.JToggleButton btnAnoA7;
    private javax.swing.JToggleButton btnAnoA8;
    private javax.swing.JToggleButton btnAnoA9;
    private javax.swing.JButton btnArqFotoPre;
    private javax.swing.JButton btnCamPre;
    private javax.swing.JButton btnEncPre;
    private javax.swing.JButton btnFotoA1;
    private javax.swing.JButton btnFotoA10;
    private javax.swing.JButton btnFotoA11;
    private javax.swing.JButton btnFotoA12;
    private javax.swing.JButton btnFotoA13;
    private javax.swing.JButton btnFotoA14;
    private javax.swing.JButton btnFotoA15;
    private javax.swing.JButton btnFotoA16;
    private javax.swing.JButton btnFotoA17;
    private javax.swing.JButton btnFotoA18;
    private javax.swing.JButton btnFotoA19;
    private javax.swing.JButton btnFotoA2;
    private javax.swing.JButton btnFotoA20;
    private javax.swing.JButton btnFotoA21;
    private javax.swing.JButton btnFotoA22;
    private javax.swing.JButton btnFotoA23;
    private javax.swing.JButton btnFotoA24;
    private javax.swing.JButton btnFotoA25;
    private javax.swing.JButton btnFotoA26;
    private javax.swing.JButton btnFotoA27;
    private javax.swing.JButton btnFotoA28;
    private javax.swing.JButton btnFotoA29;
    private javax.swing.JButton btnFotoA3;
    private javax.swing.JButton btnFotoA30;
    private javax.swing.JButton btnFotoA31;
    private javax.swing.JButton btnFotoA32;
    private javax.swing.JButton btnFotoA33;
    private javax.swing.JButton btnFotoA34;
    private javax.swing.JButton btnFotoA35;
    private javax.swing.JButton btnFotoA36;
    private javax.swing.JButton btnFotoA4;
    private javax.swing.JButton btnFotoA5;
    private javax.swing.JButton btnFotoA6;
    private javax.swing.JButton btnFotoA7;
    private javax.swing.JButton btnFotoA8;
    private javax.swing.JButton btnFotoA9;
    public static javax.swing.JButton btnSalFotPre11;
    private javax.swing.ButtonGroup gbrA20;
    private javax.swing.ButtonGroup gbrA21;
    private javax.swing.ButtonGroup gbrA22;
    private javax.swing.ButtonGroup gbrA23;
    private javax.swing.ButtonGroup gbrA24;
    private javax.swing.ButtonGroup gbrA25;
    private javax.swing.ButtonGroup gbrA26;
    private javax.swing.ButtonGroup gbrA27;
    private javax.swing.ButtonGroup gbrA28;
    private javax.swing.ButtonGroup gbrA29;
    private javax.swing.ButtonGroup gbrA30;
    private javax.swing.ButtonGroup gbrA31;
    private javax.swing.ButtonGroup gbrA32;
    private javax.swing.ButtonGroup grbA1;
    private javax.swing.ButtonGroup grbA10;
    private javax.swing.ButtonGroup grbA11;
    private javax.swing.ButtonGroup grbA12;
    private javax.swing.ButtonGroup grbA13;
    private javax.swing.ButtonGroup grbA14;
    private javax.swing.ButtonGroup grbA15;
    private javax.swing.ButtonGroup grbA16;
    private javax.swing.ButtonGroup grbA17;
    private javax.swing.ButtonGroup grbA18;
    private javax.swing.ButtonGroup grbA19;
    private javax.swing.ButtonGroup grbA2;
    private javax.swing.ButtonGroup grbA3;
    private javax.swing.ButtonGroup grbA33;
    private javax.swing.ButtonGroup grbA34;
    private javax.swing.ButtonGroup grbA35;
    private javax.swing.ButtonGroup grbA36;
    private javax.swing.ButtonGroup grbA37;
    private javax.swing.ButtonGroup grbA38;
    private javax.swing.ButtonGroup grbA39;
    private javax.swing.ButtonGroup grbA4;
    private javax.swing.ButtonGroup grbA40;
    private javax.swing.ButtonGroup grbA41;
    private javax.swing.ButtonGroup grbA42;
    private javax.swing.ButtonGroup grbA43;
    private javax.swing.ButtonGroup grbA44;
    private javax.swing.ButtonGroup grbA45;
    private javax.swing.ButtonGroup grbA46;
    private javax.swing.ButtonGroup grbA47;
    private javax.swing.ButtonGroup grbA48;
    private javax.swing.ButtonGroup grbA5;
    private javax.swing.ButtonGroup grbA6;
    private javax.swing.ButtonGroup grbA7;
    private javax.swing.ButtonGroup grbA8;
    private javax.swing.ButtonGroup grbA9;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblFotEltMen11;
    private javax.swing.JRadioButton rbtA1;
    private javax.swing.JRadioButton rbtA10;
    private javax.swing.JRadioButton rbtA11;
    private javax.swing.JRadioButton rbtA12;
    private javax.swing.JRadioButton rbtA13;
    private javax.swing.JRadioButton rbtA14;
    private javax.swing.JRadioButton rbtA15;
    private javax.swing.JRadioButton rbtA16;
    private javax.swing.JRadioButton rbtA17;
    private javax.swing.JRadioButton rbtA18;
    private javax.swing.JRadioButton rbtA19;
    private javax.swing.JRadioButton rbtA2;
    private javax.swing.JRadioButton rbtA20;
    private javax.swing.JRadioButton rbtA21;
    private javax.swing.JRadioButton rbtA22;
    private javax.swing.JRadioButton rbtA23;
    private javax.swing.JRadioButton rbtA24;
    private javax.swing.JRadioButton rbtA25;
    private javax.swing.JRadioButton rbtA26;
    private javax.swing.JRadioButton rbtA27;
    private javax.swing.JRadioButton rbtA28;
    private javax.swing.JRadioButton rbtA29;
    private javax.swing.JRadioButton rbtA3;
    private javax.swing.JRadioButton rbtA30;
    private javax.swing.JRadioButton rbtA31;
    private javax.swing.JRadioButton rbtA32;
    private javax.swing.JRadioButton rbtA33;
    private javax.swing.JRadioButton rbtA34;
    private javax.swing.JRadioButton rbtA35;
    private javax.swing.JRadioButton rbtA36;
    private javax.swing.JRadioButton rbtA37;
    private javax.swing.JRadioButton rbtA38;
    private javax.swing.JRadioButton rbtA39;
    private javax.swing.JRadioButton rbtA4;
    private javax.swing.JRadioButton rbtA40;
    private javax.swing.JRadioButton rbtA41;
    private javax.swing.JRadioButton rbtA42;
    private javax.swing.JRadioButton rbtA43;
    private javax.swing.JRadioButton rbtA44;
    private javax.swing.JRadioButton rbtA45;
    private javax.swing.JRadioButton rbtA46;
    private javax.swing.JRadioButton rbtA47;
    private javax.swing.JRadioButton rbtA48;
    private javax.swing.JRadioButton rbtA49;
    private javax.swing.JRadioButton rbtA5;
    private javax.swing.JRadioButton rbtA50;
    private javax.swing.JRadioButton rbtA51;
    private javax.swing.JRadioButton rbtA52;
    private javax.swing.JRadioButton rbtA53;
    private javax.swing.JRadioButton rbtA54;
    private javax.swing.JRadioButton rbtA55;
    private javax.swing.JRadioButton rbtA56;
    private javax.swing.JRadioButton rbtA57;
    private javax.swing.JRadioButton rbtA58;
    private javax.swing.JRadioButton rbtA59;
    private javax.swing.JRadioButton rbtA6;
    private javax.swing.JRadioButton rbtA60;
    private javax.swing.JRadioButton rbtA61;
    private javax.swing.JRadioButton rbtA62;
    private javax.swing.JRadioButton rbtA63;
    private javax.swing.JRadioButton rbtA64;
    private javax.swing.JRadioButton rbtA65;
    private javax.swing.JRadioButton rbtA66;
    private javax.swing.JRadioButton rbtA7;
    private javax.swing.JRadioButton rbtA71;
    private javax.swing.JRadioButton rbtA72;
    private javax.swing.JRadioButton rbtA73;
    private javax.swing.JRadioButton rbtA74;
    private javax.swing.JRadioButton rbtA75;
    private javax.swing.JRadioButton rbtA76;
    private javax.swing.JRadioButton rbtA8;
    private javax.swing.JRadioButton rbtA9;
    private javax.swing.JScrollPane txpAAnot1;
    private javax.swing.JScrollPane txpAAnot10;
    private javax.swing.JScrollPane txpAAnot11;
    private javax.swing.JScrollPane txpAAnot12;
    private javax.swing.JScrollPane txpAAnot13;
    private javax.swing.JScrollPane txpAAnot14;
    private javax.swing.JScrollPane txpAAnot15;
    private javax.swing.JScrollPane txpAAnot16;
    private javax.swing.JScrollPane txpAAnot17;
    private javax.swing.JScrollPane txpAAnot18;
    private javax.swing.JScrollPane txpAAnot19;
    private javax.swing.JScrollPane txpAAnot2;
    private javax.swing.JScrollPane txpAAnot20;
    private javax.swing.JScrollPane txpAAnot21;
    private javax.swing.JScrollPane txpAAnot22;
    private javax.swing.JScrollPane txpAAnot23;
    private javax.swing.JScrollPane txpAAnot24;
    private javax.swing.JScrollPane txpAAnot25;
    private javax.swing.JScrollPane txpAAnot26;
    private javax.swing.JScrollPane txpAAnot27;
    private javax.swing.JScrollPane txpAAnot28;
    private javax.swing.JScrollPane txpAAnot29;
    private javax.swing.JScrollPane txpAAnot3;
    private javax.swing.JScrollPane txpAAnot30;
    private javax.swing.JScrollPane txpAAnot31;
    private javax.swing.JScrollPane txpAAnot32;
    private javax.swing.JScrollPane txpAAnot33;
    private javax.swing.JScrollPane txpAAnot34;
    private javax.swing.JScrollPane txpAAnot35;
    private javax.swing.JScrollPane txpAAnot36;
    private javax.swing.JScrollPane txpAAnot4;
    private javax.swing.JScrollPane txpAAnot5;
    private javax.swing.JScrollPane txpAAnot6;
    private javax.swing.JScrollPane txpAAnot7;
    private javax.swing.JScrollPane txpAAnot8;
    private javax.swing.JScrollPane txpAAnot9;
    private javax.swing.JButton txtCanFot;
    private javax.swing.JTextField txtCodEquPre;
    private javax.swing.JTextField txtDatFin;
    private javax.swing.JTextField txtDatHor;
    public static javax.swing.JTextField txtDatIni11;
    private javax.swing.JTextArea txtEquPr;
    public static javax.swing.JTextField txtFotPre11;
    private javax.swing.JTextField txtIdPreEle;
    private javax.swing.JTextField txtNomTec;
    private javax.swing.JTextArea txtSetPre;
    private javax.swing.JTextField txtStaPre;
    private javax.swing.JTextField txtTemPre;
    private javax.swing.JTextArea txtaA1;
    private javax.swing.JTextArea txtaA10;
    private javax.swing.JTextArea txtaA11;
    private javax.swing.JTextArea txtaA12;
    private javax.swing.JTextArea txtaA13;
    private javax.swing.JTextArea txtaA14;
    private javax.swing.JTextArea txtaA15;
    private javax.swing.JTextArea txtaA16;
    private javax.swing.JTextArea txtaA17;
    private javax.swing.JTextArea txtaA18;
    private javax.swing.JTextArea txtaA19;
    private javax.swing.JTextArea txtaA2;
    private javax.swing.JTextArea txtaA20;
    private javax.swing.JTextArea txtaA21;
    private javax.swing.JTextArea txtaA22;
    private javax.swing.JTextArea txtaA23;
    private javax.swing.JTextArea txtaA24;
    private javax.swing.JTextArea txtaA25;
    private javax.swing.JTextArea txtaA26;
    private javax.swing.JTextArea txtaA27;
    private javax.swing.JTextArea txtaA28;
    private javax.swing.JTextArea txtaA29;
    private javax.swing.JTextArea txtaA3;
    private javax.swing.JTextArea txtaA30;
    private javax.swing.JTextArea txtaA31;
    private javax.swing.JTextArea txtaA32;
    private javax.swing.JTextArea txtaA33;
    private javax.swing.JTextArea txtaA34;
    private javax.swing.JTextArea txtaA35;
    private javax.swing.JTextArea txtaA36;
    private javax.swing.JTextArea txtaA4;
    private javax.swing.JTextArea txtaA5;
    private javax.swing.JTextArea txtaA6;
    private javax.swing.JTextArea txtaA7;
    private javax.swing.JTextArea txtaA8;
    private javax.swing.JTextArea txtaA9;
    // End of variables declaration//GEN-END:variables
}
