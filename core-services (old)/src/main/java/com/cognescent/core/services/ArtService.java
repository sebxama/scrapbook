package com.cognescent.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArtService {

/*
	
    @Autowired
	private ArtistDao artistDao;

	@Autowired
	private PaintingDao paintingDao;

	@Transactional
	public Artist createArtist(String firstName, String lastName) {
		Artist artist = new Artist();
		artist.setFirstName(firstName);
		artist.setLastName(lastName);
		return artistDao.save(artist);
	}

	@Transactional
	public Painting createPainting(String title, String technique, IRI artist) {
		Painting painting = new Painting();
		painting.setTitle(title);
		painting.setTechnique(technique);
		painting.setArtistId(artist);
		return paintingDao.save(painting);
	}

	@Transactional
	public List<Painting> getPaintings() {
		return paintingDao.list();
	}

	@Transactional
	public List<Artist> getArtists() {
		return artistDao.list();
	}

	@Transactional
	public Set<Artist> getArtistsWithoutPaintings(){
		return artistDao.getArtistsWithoutPaintings();
	}

*/

}
