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
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.prjmanutencao.telas.TelaEletricaMen.foto_ele;
import static com.prjmanutencao.telas.TelaHidraulicaMen1.foto_ele1;
import static com.prjmanutencao.telas.TelaCivilMen1.foto_ele2;
import static com.prjmanutencao.telas.TelaRefrigeracaoMen1.foto_ele3;

import static com.prjmanutencao.telas.TelaEletricaMen.lblFotEltMen;
import static com.prjmanutencao.telas.TelaEletricaMen.txtFotPre;
import static com.prjmanutencao.telas.TelaEletricaMen.btnSalFotPre;

import static com.prjmanutencao.telas.TelaHidraulicaMen1.btnSalFotPre1;
import static com.prjmanutencao.telas.TelaHidraulicaMen1.lblFotEltMen1;
import static com.prjmanutencao.telas.TelaHidraulicaMen1.txtFotPre1;

import static com.prjmanutencao.telas.TelaCivilMen1.btnSalFotPre2;
import static com.prjmanutencao.telas.TelaCivilMen1.lblFotEltMen2;
import static com.prjmanutencao.telas.TelaCivilMen1.txtFotPre2;

import static com.prjmanutencao.telas.TelaRefrigeracaoMen1.btnSalFotPre3;
import static com.prjmanutencao.telas.TelaRefrigeracaoMen1.lblFotEltMen3;
import static com.prjmanutencao.telas.TelaRefrigeracaoMen1.txtFotPre3;

import static com.prjmanutencao.telas.TelaEletricaTrimestral.foto_ele4;
import static com.prjmanutencao.telas.TelaHidraulicaTrimestral.foto_ele5;
import static com.prjmanutencao.telas.TelaCivilTrimestral.foto_ele6;
import static com.prjmanutencao.telas.TelaRefrigeracaoTrimestral.foto_ele7;

import static com.prjmanutencao.telas.TelaEletricaTrimestral.lblFotEltMen4;
import static com.prjmanutencao.telas.TelaEletricaTrimestral.txtFotPre4;
import static com.prjmanutencao.telas.TelaEletricaTrimestral.btnSalFotPre4;

import static com.prjmanutencao.telas.TelaHidraulicaTrimestral.btnSalFotPre5;
import static com.prjmanutencao.telas.TelaHidraulicaTrimestral.lblFotEltMen5;
import static com.prjmanutencao.telas.TelaHidraulicaTrimestral.txtFotPre5;

import static com.prjmanutencao.telas.TelaCivilTrimestral.btnSalFotPre6;
import static com.prjmanutencao.telas.TelaCivilTrimestral.lblFotEltMen6;
import static com.prjmanutencao.telas.TelaCivilTrimestral.txtFotPre6;

import static com.prjmanutencao.telas.TelaRefrigeracaoTrimestral.btnSalFotPre7;
import static com.prjmanutencao.telas.TelaRefrigeracaoTrimestral.lblFotEltMen7;
import static com.prjmanutencao.telas.TelaRefrigeracaoTrimestral.txtFotPre7;

import static com.prjmanutencao.telas.TelaEletricaSem.foto_ele8;
import static com.prjmanutencao.telas.TelaHidraulicaSem.foto_ele10;
import static com.prjmanutencao.telas.TelaCivilSem.foto_ele9;
import static com.prjmanutencao.telas.TelaRefrigeracaoSem.foto_ele11;

import static com.prjmanutencao.telas.TelaEletricaSem.lblFotEltMen8;
import static com.prjmanutencao.telas.TelaEletricaSem.txtFotPre8;
import static com.prjmanutencao.telas.TelaEletricaSem.btnSalFotPre8;

import static com.prjmanutencao.telas.TelaHidraulicaSem.btnSalFotPre10;
import static com.prjmanutencao.telas.TelaHidraulicaSem.lblFotEltMen10;
import static com.prjmanutencao.telas.TelaHidraulicaSem.txtFotPre10;

import static com.prjmanutencao.telas.TelaCivilSem.btnSalFotPre9;
import static com.prjmanutencao.telas.TelaCivilSem.lblFotEltMen9;
import static com.prjmanutencao.telas.TelaCivilSem.txtFotPre9;

import static com.prjmanutencao.telas.TelaRefrigeracaoSem.btnSalFotPre11;
import static com.prjmanutencao.telas.TelaRefrigeracaoSem.lblFotEltMen11;
import static com.prjmanutencao.telas.TelaRefrigeracaoSem.txtFotPre11;
import java.awt.Toolkit;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe TelaWebCam1
 *
 * Representa a interface gráfica para captura de imagem via webcam. Estende
 * javax.swing.JFrame e incorpora funcionalidades de captura, exibição e
 * armazenamento de imagem.
 *
 * Atributos: - dimensao_defaul: dimensão padrão da janela ou painel de
 * exibição. - WEBCAM: instância da webcam utilizada para captura. - executando:
 * flag que indica se a captura está ativa. - Imagem: nome ou caminho da imagem
 * capturada. - nome_foto: nome da imagem a ser salva (estático e
 * compartilhado). - tipo_prev: tipo de preventiva associada à captura (estático
 * e compartilhado). - BYTES_IMAGEM1: array de bytes da imagem capturada
 * (estático e compartilhado). - conexao: conexão com o banco de dados. - pst:
 * comando preparado para execução de SQL. - rs: conjunto de resultados de uma
 * consulta SQL.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaWebCam1 extends javax.swing.JFrame {

    private Dimension dimensao_defaul;
    private Webcam WEBCAM;
    boolean executando = true;
    String Imagem;
    public static String nome_foto = null;
    public static String tipo_prev = null;

    public static byte[] BYTES_IMAGEM1 = null;
    String caminho = "/../systemOs"; // Caminho relativo

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Construtor da classe TelaWebCam1
     *
     * Responsável por inicializar a interface de captura de imagem via webcam.
     * Ao instanciar a classe, o construtor executa as seguintes ações:
     *
     * - Chama o método initComponents() para configurar os componentes da
     * interface gráfica. - Define o ícone da janela com setIcon(). - Estabelece
     * a conexão com o banco de dados através de ModuloConexao.conector(). -
     * Desabilita os botões de captura e parada da câmera (btnCapCam e
     * btnParCam). - Inicia o processo de configuração da webcam com o método
     * Iniciar().
     */
    public TelaWebCam1() {
        initComponents();
        setIcon();
        conexao = ModuloConexao.conector();
        btnCapCam.setEnabled(false);
        btnParCam.setEnabled(false);
        Iniciar();

    }

    /**
     * Método `iniciar_camera`. Este método inicializa e abre a webcam,
     * configura os elementos da interface gráfica relacionados à câmera e
     * inicia o fluxo de vídeo para exibição em tempo real no componente
     * associado. A execução ocorre em uma thread separada para garantir que a
     * interface gráfica permaneça responsiva.
     *
     * Funcionalidades: - **Execução em Thread Separada**: - Utiliza uma nova
     * thread para realizar as operações de inicialização da câmera, evitando
     * bloqueios na interface.
     *
     * - **Configuração Inicial da Câmera**: - Define `executando = true` para
     * indicar que a câmera está ativa. - Desabilita o botão de inicialização da
     * câmera (`btnIniCam`) para evitar múltiplas inicializações. - Exibe a
     * mensagem `"Iniciando..."` no componente `lblImaCam` enquanto a câmera é
     * configurada.
     *
     * - **Inicialização da Webcam**: - `Webcam.setDriver(new
     * WebcamDefaultDriver())`: Configura o driver padrão para a webcam. -
     * `WEBCAM = Webcam.getDefault()`: Seleciona a webcam padrão do sistema. -
     * `WEBCAM.open()`: Abre a câmera e aloca os recursos necessários para seu
     * funcionamento. - Limpa a mensagem em `lblImaCam` ao concluir a
     * inicialização.
     *
     * - **Início do Fluxo de Vídeo**: - Chama o método `Inicializa_video()`
     * para capturar e exibir o vídeo da câmera em tempo real.
     *
     * - **Atualização da Interface**: - Habilita os botões `btnCapCam`
     * (capturar imagem) e `btnParCam` (parar câmera) após a inicialização.
     *
     * Fluxo do Método: 1. Cria e executa uma thread separada para inicializar a
     * câmera. 2. Configura a câmera e os componentes da interface gráfica. 3.
     * Inicia o fluxo de vídeo. 4. Habilita os botões relevantes e exibe o
     * status adequado na interface.
     */
    private void iniciar_camera() {

        new Thread() {
            @Override
            public void run() {
                executando = true;
                btnIniCam.setEnabled(false);
                lblImaCam.setText("Iniciando...");
                Webcam.setDriver(new WebcamDefaultDriver());
                WEBCAM = Webcam.getDefault();
                WEBCAM.open();
                lblImaCam.setText("");
                Inicializa_video();
                btnCapCam.setEnabled(true);
                btnParCam.setEnabled(true);
            }
        }.start();

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);
            foto_ele = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen.getWidth(), lblFotEltMen.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen.setIcon(icon);

            btnSalFotPre.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem1() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre1.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele1 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen1.getWidth(), lblFotEltMen1.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen1.setIcon(icon);

            btnSalFotPre1.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem2() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre2.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele2 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen2.getWidth(), lblFotEltMen2.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen2.setIcon(icon);

            btnSalFotPre2.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem3() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre3.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele3 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen3.getWidth(), lblFotEltMen3.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen3.setIcon(icon);

            btnSalFotPre3.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem4() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre4.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele4 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen4.getWidth(), lblFotEltMen4.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen4.setIcon(icon);

            btnSalFotPre4.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem5() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre5.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele5 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen5.getWidth(), lblFotEltMen5.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen5.setIcon(icon);

            btnSalFotPre5.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem6() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre6.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele6 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO
////
            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen6.getWidth(), lblFotEltMen6.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen6.setIcon(icon);

            btnSalFotPre6.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem7() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre7.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele7 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen7.getWidth(), lblFotEltMen7.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen7.setIcon(icon);

            btnSalFotPre7.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem8() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre8.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele8 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO
////
            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen8.getWidth(), lblFotEltMen8.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen8.setIcon(icon);

            btnSalFotPre8.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem10() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre10.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele10 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen10.getWidth(), lblFotEltMen10.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen10.setIcon(icon);

            btnSalFotPre10.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem9() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre9.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele9 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen9.getWidth(), lblFotEltMen9.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen9.setIcon(icon);

            btnSalFotPre9.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método capturar_imagem
     *
     * Responsável por capturar uma imagem da webcam, redimensioná-la, salvar em
     * disco, converter para bytes e exibir na interface gráfica.
     *
     * Etapas executadas: 1. Gera um número aleatório para nomear a imagem. 2.
     * Captura a imagem da webcam e armazena em um array de bytes. 3. Converte
     * os bytes em BufferedImage. 4. Redimensiona a imagem para 1080x720 pixels.
     * 5. Atualiza o campo txtFotPre com o número gerado. 6. Garante que o
     * diretório de destino existe. 7. Salva a imagem redimensionada como
     * arquivo JPG. 8. Converte novamente a imagem para bytes e armazena em
     * BYTES_IMAGEM1. 9. Exibe a imagem redimensionada no componente
     * lblFotEltMen. 10. Habilita o botão de salvar imagem. 11. Encerra a
     * captura da webcam e fecha a janela.
     *
     * @param evt o evento gerado pela ação de captura
     */
    private void capturar_imagem11() {

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 1080, Nova_Altura = 720; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtFotPre11.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            foto_ele11 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);
            BYTES_IMAGEM1 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen11.getWidth(), lblFotEltMen11.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen11.setIcon(icon);

            btnSalFotPre11.setEnabled(true);

            WEBCAM.close();
            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método parar_camera
     *
     * Responsável por encerrar a captura da webcam e restaurar o estado da
     * interface. Executa as seguintes ações dentro de uma nova thread:
     *
     * - Define a flag `executando` como false para indicar que a captura foi
     * interrompida. - Fecha a instância da webcam com `WEBCAM.close()`. -
     * Remove o ícone exibido no componente `lblImaCam`. - Habilita o botão de
     * iniciar câmera (`btnIniCam`) e desabilita os botões de captura
     * (`btnCapCam`) e parada (`btnParCam`).
     *
     * Essa abordagem garante que o encerramento da câmera ocorra de forma
     * assíncrona, evitando travamentos na interface gráfica.
     */
    private void parar_camera() {

        new Thread() {
            public void run() {
                executando = false;
                WEBCAM.close();

                lblImaCam.setIcon(null);
                btnIniCam.setEnabled(true);
                btnCapCam.setEnabled(false);
                btnParCam.setEnabled(false);
            }
        }.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        lblImaCam = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        btnIniCam = new javax.swing.JButton();
        btnParCam = new javax.swing.JButton();
        btnCapCam = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Webcam System Preventiva");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));

        lblImaCam.setBackground(new java.awt.Color(102, 102, 102));
        lblImaCam.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        lblImaCam.setForeground(new java.awt.Color(153, 255, 0));
        lblImaCam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImaCam.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        lblImaCam.setOpaque(true);
        jLayeredPane1.add(lblImaCam);
        lblImaCam.setBounds(10, 10, 280, 280);

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(100, 0, 300, 300);

        jLayeredPane2.setBackground(new java.awt.Color(153, 153, 153));
        jLayeredPane2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        jLayeredPane2.setOpaque(true);

        btnIniCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Start.png"))); // NOI18N
        btnIniCam.setToolTipText("Iniciar WebCam");
        btnIniCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnIniCam);
        btnIniCam.setBounds(18, 10, 64, 64);

        btnParCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Stop.png"))); // NOI18N
        btnParCam.setToolTipText("Parar WebCam");
        btnParCam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnParCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnParCam);
        btnParCam.setBounds(18, 90, 64, 64);

        btnCapCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Captura.png"))); // NOI18N
        btnCapCam.setToolTipText("Captura");
        btnCapCam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnCapCam);
        btnCapCam.setBounds(18, 170, 64, 64);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/webcam.png"))); // NOI18N
        jLabel1.setToolTipText("Webcam System");
        jLayeredPane2.add(jLabel1);
        jLabel1.setBounds(26, 240, 50, 50);

        getContentPane().add(jLayeredPane2);
        jLayeredPane2.setBounds(0, 0, 100, 300);

        setSize(new java.awt.Dimension(416, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método btnIniCamActionPerformed
     *
     * Acionado ao clicar no botão de iniciar câmera. Executa o método
     * iniciar_camera(), responsável por ativar a webcam e iniciar a exibição da
     * imagem ao vivo. Normalmente, esse método também atualiza o estado da
     * interface, habilitando/desabilitando botões conforme necessário.
     *
     * @param evt o evento gerado pelo clique no botão de iniciar câmera
     */
    private void btnIniCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniCamActionPerformed

        iniciar_camera();
    }//GEN-LAST:event_btnIniCamActionPerformed

    /**
     * Método btnCapCamActionPerformed
     *
     * Acionado ao clicar no botão de captura da câmera. Verifica o valor da
     * variável `tipo_prev` para determinar o tipo de preventiva em execução e
     * chama o método correspondente para capturar a imagem adequada.
     *
     * Tipos de preventiva e métodos chamados: - "Eletrica - Mensal" →
     * capturar_imagem() - "Hidráulica - Mensal" → capturar_imagem1() - "Civil -
     * Mensal" → capturar_imagem2() - "Refrigeração - Mensal" →
     * capturar_imagem3() - "Eletrica - Trimestral" → capturar_imagem4() -
     * "Hidráulica - Trimestral" → capturar_imagem5() - "Civil - Trimestral" →
     * capturar_imagem6() - "Refrigeração - Trimestral" → capturar_imagem7() -
     * "Eletrica - Semestral" → capturar_imagem8() - "Hidráulica - Semestral" →
     * capturar_imagem10() - "Civil - Semestral" → capturar_imagem9() -
     * "Refrigeração - Semestral" → capturar_imagem11()
     *
     * Essa estrutura garante que a imagem capturada esteja alinhada com o tipo
     * de manutenção preventiva selecionado.
     *
     * @param evt o evento gerado pelo clique no botão de captura da câmera
     */
    private void btnCapCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapCamActionPerformed

        if (tipo_prev.equals("Eletrica - Mensal")) {
            capturar_imagem();
        }
        if (tipo_prev.equals("Hidráulica - Mensal")) {
            capturar_imagem1();
        }
        if (tipo_prev.equals("Civil - Mensal")) {
            capturar_imagem2();
        }
        if (tipo_prev.equals("Refrigeração - Mensal")) {
            capturar_imagem3();
        }
        if (tipo_prev.equals("Eletrica - Trimestral")) {
            capturar_imagem4();
        }
        if (tipo_prev.equals("Hidráulica - Trimestral")) {
            capturar_imagem5();
        }
        if (tipo_prev.equals("Civil - Trimestral")) {
            capturar_imagem6();
        }
        if (tipo_prev.equals("Refrigeração - Trimestral")) {
            capturar_imagem7();
        }
        if (tipo_prev.equals("Eletrica - Semestral")) {
            capturar_imagem8();
        }
        if (tipo_prev.equals("Hidráulica - Semestral")) {
            capturar_imagem10();
        }
        if (tipo_prev.equals("Civil - Semestral")) {
            capturar_imagem9();
        }
        if (tipo_prev.equals("Refrigeração - Semestral")) {
            capturar_imagem11();
        }


    }//GEN-LAST:event_btnCapCamActionPerformed

    /**
     * Método btnParCamActionPerformed
     *
     * Acionado ao clicar no botão de parada da câmera. Executa o método
     * parar_camera() para encerrar a captura da webcam, liberar os recursos e
     * atualizar a interface.
     *
     * @param evt o evento gerado pelo clique no botão de parada da câmera
     */
    private void btnParCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParCamActionPerformed

        parar_camera();
    }//GEN-LAST:event_btnParCamActionPerformed

    /**
     * Método formWindowClosing
     *
     * Acionado ao fechar a janela da interface. Executa o método parar_camera()
     * para garantir que a webcam seja encerrada corretamente antes do
     * fechamento da aplicação.
     *
     * @param evt o evento gerado pelo fechamento da janela
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        parar_camera();
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(TelaWebCam1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaWebCam1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaWebCam1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaWebCam1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaWebCam1().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapCam;
    private javax.swing.JButton btnIniCam;
    private javax.swing.JButton btnParCam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLabel lblImaCam;
    // End of variables declaration//GEN-END:variables

    /**
     * Método Iniciar
     *
     * Responsável por configurar a webcam utilizada na aplicação. Executa as
     * seguintes ações: - Define a dimensão padrão da webcam como VGA. - Obtém a
     * lista de webcams disponíveis e seleciona a última da lista. - Define a
     * resolução da webcam selecionada com a dimensão padrão.
     *
     * Comentários adicionais indicam alternativas para definir resolução
     * personalizada ou listar resoluções suportadas pela webcam.
     *
     * @throws WebcamException caso ocorra erro na inicialização da webcam
     */
    private void Iniciar() {
        try {
//            dimensao_defaul = new Dimension(176, 144); //UTILIZO A DIMENSÃO DE MINHA PREFERÊNCIA
            dimensao_defaul = WebcamResolution.VGA.getSize();//UTILIZA A DIMENSÃO PADRÃO DA WEBCAM
//            WEBCAM = Webcam.getDefault(); //UTILIZA A WEBCAM PADRÃO DO COMPUTADOR            
            List list = Webcam.getWebcams();
            for (int i = 0; i < list.size(); i++) {
                WEBCAM = (Webcam) list.get(i); //UTILIZA A ÚLTIMA WEBCAM ENCONTRADA, SE EXISTIR MAIS DE UMA PEGA A ÚLTIMA
            }
            WEBCAM.setViewSize(dimensao_defaul);

//            for (Dimension dimensao : WEBCAM.getViewSizes()) { // AQUI EU CONSIGO SABER QUAIS SÃO RESOLUÇÕES SUPORTADA PELA WEBCAM
//                System.out.println("Largura: " + dimensao.getWidth() + "  Altura: " + dimensao.getHeight());
//            }
        } catch (WebcamException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Método Inicializa_video
     *
     * Responsável por iniciar a captura contínua de imagens da webcam em uma
     * nova thread. Enquanto a flag `executando` estiver verdadeira: - Captura a
     * imagem da webcam. - Redimensiona a imagem para se ajustar ao componente
     * `lblImaCam`. - Define a imagem como ícone do componente `lblImaCam`. -
     * Aguarda 50 milissegundos antes de capturar a próxima imagem.
     *
     * @throws InterruptedException caso a thread seja interrompida durante o
     * sleep
     */
    private void Inicializa_video() {
        new Thread() {
            @Override
            public void run() {
                while (true && executando) {
                    try {
                        Image imagem = WEBCAM.getImage();
                        ImageIcon icon = new ImageIcon(imagem);
                        icon.setImage(icon.getImage().getScaledInstance(lblImaCam.getWidth(), lblImaCam.getHeight(), Image.SCALE_SMOOTH));
                        lblImaCam.setIcon(icon);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
            }
        }.start();
    }

    /**
     * Método setIcon
     *
     * Define o ícone da janela principal da aplicação. Utiliza a imagem
     * localizada em "/com/prjmanutencao/icones/iconPrincipal.png" como ícone da
     * janela, através do método `setIconImage`.
     *
     * A imagem deve estar disponível no classpath da aplicação.
     */
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png")));
    }
}
