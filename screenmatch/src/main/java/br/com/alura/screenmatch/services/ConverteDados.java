package br.com.alura.screenmatch.services;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

public class ConverteDados implements IConverteDados{
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public <T> T obterDados(String json, Class<T> classe) {
		
		try {
		return mapper.readValue(json, classe);
	} catch(JsonNodeException e) {
		throw new RuntimeException(e);
	}
	
	}
	
}
