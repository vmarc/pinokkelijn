/**
*	Site-specific configuration settings for Highslide JS
*/
hs.graphicsDir = 'highslide/graphics/';
hs.showCredits = false;
hs.outlineType = 'custom';
hs.dimmingOpacity = 0.75;
hs.fadeInOut = true;
hs.align = 'center';
hs.useBox = true;
hs.width = 500;
hs.height = 500;


// Add the slideshow controller
hs.addSlideshow({
	slideshowGroup: 'group1',
	interval: 5000,
	repeat: false,
	useControls: true,
	fixedControls: 'fit',
	overlayOptions: {
		className: 'controls-in-heading',
		opacity: 1,
		position: 'bottom right',
		offsetX: 0,
		offsetY: 20,
		hideOnMouseOut: false
	}
});

// Dutch language strings
hs.lang = {
	cssDirection: 'ltr',
	loadingText: 'Laden...',
	loadingTitle: 'Klik om te annuleren',
	focusTitle: 'Klik om naar voren te brengen',
	fullExpandTitle: 'Vergroot naar origineel (f)',
	creditsText: 'Powered door <i>Highslide JS</i>',
	creditsTitle: 'Ga naar de Highslide JS homepage',
	previousText: 'Vorige',
	nextText: 'Volgende',
	moveText: 'Verplaats',
	closeText: 'Sluiten',
	closeTitle: 'Sluiten (esc)',
	resizeTitle: 'Afmeting wijzigen',
	playText: 'Afspelen',
	playTitle: 'Start diashow (spatiebalk)',
	pauseText: 'Pauze',
	pauseTitle: 'Diashow pauzeren (spatiebalk)',
	previousTitle: 'Vorige (pijl naar links)',
	nextTitle: 'Volgende (pijl naar rechts)',
	moveTitle: 'Verplaats',
	fullExpandText: 'Volledige grootte',
	number: 'Beeld %1 van %2',
	restoreTitle: 'Klik om te sluiten, klik en sleep om te verplaatsen. Gebruik pijltjes toetsen voor volgende en vorige.'
};

// gallery config object
var config1 = {
	slideshowGroup: 'group1',
	numberPosition: 'caption',
	transitions: ['expand', 'crossfade']
};
