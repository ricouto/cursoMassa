package br.ce.wcaquino.estrategia1;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuarioServicoTest {
	
	private UsuarioService service = new UsuarioService();
	private static Usuario usuarioGlobal;
	
	@Test
	public void test1_inserir() throws Exception {
		Usuario usuario = new Usuario("Usuario estrategia #1", "usuario1@gmail.com", "senha");
		usuarioGlobal = service.salvar(usuario);
		Assert.assertNotNull(usuarioGlobal.getId());
	}
	
	@Test
	public void test2_consultar() throws Exception {
		Usuario usuario = service.findById(usuarioGlobal.getId());
		Assert.assertEquals("Usuario estrategia #1", usuario.getNome());
	}
	
	@Test
	public void test3_alterar() throws Exception {
		Usuario usuario = service.findById(usuarioGlobal.getId());
		usuario.setNome("Usuario Alterado");
		Usuario usuarioAlterado = service.salvar(usuario);
		Assert.assertEquals("Usuario Alterado", usuarioAlterado.getNome());
	}
	
	@Test
	public void test4_excluir() throws Exception {
		service.delete(usuarioGlobal);
		Usuario usuarioRemovido = service.findById(usuarioGlobal.getId());
		Assert.assertNull(usuarioRemovido);
	}

}
