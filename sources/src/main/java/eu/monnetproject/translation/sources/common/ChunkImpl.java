package eu.monnetproject.translation.sources.common;


import eu.monnetproject.translation.Chunk;

public class ChunkImpl implements Chunk {
	private String chunk;

	public ChunkImpl(String chunk) {
		this.chunk = chunk;
	}

	@Override
	public String getSource() {
		return chunk;
	}


}
