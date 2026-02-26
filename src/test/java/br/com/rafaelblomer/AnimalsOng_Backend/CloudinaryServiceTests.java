package br.com.rafaelblomer.AnimalsOng_Backend;

import br.com.rafaelblomer.AnimalsOng_Backend.business.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTests {

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile multipartFile;

    @Test
    void deveFazerUploadDaImagemComSucesso() throws Exception {
        byte[] bytes = "imagem-fake".getBytes();

        when(cloudinary.uploader()).thenReturn(uploader);
        when(multipartFile.getBytes()).thenReturn(bytes);

        Map<String, Object> respostaCloudinary = Map.of(
                "secure_url", "https://cloudinary.com/imagem.jpg"
        );

        when(uploader.upload(eq(bytes), anyMap()))
                .thenReturn(respostaCloudinary);

        String url = cloudinaryService.uploadImagem(multipartFile);

        assertEquals("https://cloudinary.com/imagem.jpg", url);

        verify(cloudinary).uploader();
        verify(uploader).upload(eq(bytes), anyMap());
    }

    @Test
    void deveLancarExcecaoQuandoFalharUpload() throws Exception {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(multipartFile.getBytes()).thenThrow(new IOException("erro"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cloudinaryService.uploadImagem(multipartFile)
        );

        assertEquals("Erro ao enviar imagem", exception.getMessage());
    }

    @Test
    void deveDeletarImagemPorUrl() throws Exception {
        when(cloudinary.uploader()).thenReturn(uploader);

        String imageUrl =
                "https://res.cloudinary.com/demo/image/upload/v123456/abc123.jpg";

        cloudinaryService.deletarPorUrl(imageUrl);

        verify(uploader).destroy(
                eq("animalsong/animais/abc123"),
                anyMap()
        );
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoDeletarImagem() throws Exception {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), anyMap()))
                .thenThrow(new RuntimeException("erro"));

        String imageUrl =
                "https://res.cloudinary.com/demo/image/upload/v123456/abc123.jpg";

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cloudinaryService.deletarPorUrl(imageUrl)
        );

        assertEquals("Erro ao deletar imagem", exception.getMessage());
    }

}
