/* global _formation, _key, _team_id, _orientation, _lang, statsfc_lang, alert, twttr */

function loadLang(d, s, id, l)
{
    var lang, flang = d.getElementsByTagName(s)[0];

    if (d.getElementById(id)) {
        return;
    }

    lang     = d.createElement(s);
    lang.id  = id;
    lang.src = 'https://statsfc-4f51.kxcdn.com/widget/lang.' + l + '.js';

    flang.parentNode.insertBefore(lang, flang);
}

function translate(key, replacements)
{
    var translation;

    if (typeof statsfc_lang === 'undefined' || typeof statsfc_lang[key] === 'undefined') {
        translation = key;
    } else {
        translation = statsfc_lang[key];
    }

    if (typeof replacements === 'object') {
        for (var name in replacements) {
            translation = translation.replace('{' + name + '}', replacements[name]);
        }
    }

    return translation;
}

    function selectPlayer($playerId,$player,position,newName,newPhoto){

        $('input[name="' + position + '"]').val($playerId).attr('data-name', newName);

        var $photo    = $player.find('img');
        var $name     = $player.find('span');
        if(newPhoto){
            // default phone
            $photo.attr('src', newPhoto).attr('alt', newName);
        }else{
            $photo.attr('alt', newName);
        }

        $player.attr('data-id', $playerId);
        
        
        $name.text(newName);

        if ($playerId.length === 0) {
            $player.addClass('empty');
        } else {
            $player.removeClass('empty');
        }
    }

    /**
     * Auto set position by exitSquadPlayers
     */
    var interval;

    function defaultAutoSet(){
        var str = $('#exitPlayers').val();
        // console.log(str);
        if(str && str.length > 5){

            clearInterval(interval);
            
            var exitSquadPlayers = JSON.parse(str);
            $.each(exitSquadPlayers,function(idx,obj){
                var playerId = obj.player.id;
                var playerName = obj.player.name;
                var playerNumber = obj.playerNumber;
                var playerPosition = obj.playerPosition;
                var isSub = obj.isSubstitute;
                var photo = obj.player.picture;

                // console.log(playerId+" "+ playerName+" "+ playerNumber+" "+ playerPosition +" "+ isSub+" "+photo);
                var $player   = $('.player[data-position="' + playerPosition + '"]');
                
                selectPlayer(playerId,$player,playerPosition,playerName,photo);
            });
        }

    }

    function intervalWarpper(){
         defaultAutoSet(); 
    }





$(function() {


 
    
    interval = setInterval("intervalWarpper();",1000);
    

    var active_formation = _formation;

    var $form      = $('form');
    var $formation = $('#formation');
    var $players   = $('#players');
    var $close     = $('#close');
    var $twitter   = $('#tweet');
    var $facebook  = $('#facebook');


    // Load the translation file
    // loadLang(document, 'script', 'statsfc-lang', _lang);


    // Create a custom dropdown.
    $formation.uniform();


    // Draggable players.
    $('.player:not(.sub)').draggable({
        containment: '#pitch',
        stop: function(event, ui) {
            $('input.player_id[name="' + ui.helper.attr('data-position') + '"]').attr({ 'data-top': ui.position.top, 'data-left': ui.position.left });
        }
    });


    // Change formation.
    $formation.on('change', function() {
        var formation = $(this).val();

        $('.line, .player:not(.sub), .position:not(.sub)').hide();
        $('.player_id').removeClass('active');

        $('.line[data-formations*=",' + formation + ',"], .player[data-formations*=",' + formation + ',"]').show();
        $('.player_id[data-formations*=",' + formation + ',"]').addClass('active');

        $('#pitch').removeClass('formation-' + active_formation).addClass('formation-' + formation);

        active_formation = formation;
    }).val(active_formation).trigger('change');


    // Hide the overlay if anywhere outside it is clicked.
    $(document).on('click', function(e) {
        if (! $players.is(':visible')) {
            return;
        }

        var $target = $(e.target);

        if (/players/.test(e.target.id) || /player/.test(e.target.className) || $target.parents('.player').length > 0 || $target.parents('#players').length > 0) {
            return;
        }

        $players.addClass('closed');
    });


    // Players overlay.
    var CURRENT_POSITION;

    $('.player').on('click', function() {
       
        // Hide all players.
        $('#players #list div[data-lines]').hide().removeClass('current');
        $('#players h3').empty();

        // Show players for relevant line.
        var $player = $(this),
            line,
            position;

        if (! $player.hasClass('sub')) {
            line     = $player.attr('data-line');
            position = translate($player.attr('data-line-name')) + ' (' + translate($player.attr('data-name')) + ')';
        } else {
            line     = false;
            position = translate('Substitute');
        }

        CURRENT_POSITION = $player.attr('data-position');

        $('#players h3').text(position);
        //$('#players div' + (line !== false ? '[data-lines*=",' + line + ',"]' : '')).show();
        $('#players div').show();
        $('#list div[data-player-id="' + $(this).attr('data-id') + '"]').addClass('current');

        $('#players').removeClass('closed');
    });


    // Player selection.
    $('#list').on('click','div', function() {
        var player_id = $(this).attr('data-player-id');
        var $player   = $('.player[data-position="' + CURRENT_POSITION + '"]');
        // var $photo    = $player.find('img');
        // var $name     = $player.find('span');
        var newPhoto  = $(this).find('img').attr('src');
        var newName   = $(this).find('span').text();

        // $('input[name="' + CURRENT_POSITION + '"]').val(player_id).attr('data-name', newName);

        // $player.attr('data-id', player_id);
        // $photo.attr('src', newPhoto).attr('alt', newName);
        // $name.text(newName);

        // if (player_id.length === 0) {
        //     $player.addClass('empty');
        // } else {
        //     $player.removeClass('empty');
        // }
        selectPlayer(player_id,$player,CURRENT_POSITION,newName,newPhoto);

        $close.trigger('click');
    });

    

   


    // Close the overlay.
    $close.on('click', function(e) {
        e.preventDefault();
        $('#players').addClass('closed');
    });






    


    /*$form.on('submit', function () {
        $('select, input:hidden').each(function() {
            var val  = $(this).val();
            var top  = $(this).attr('data-top');
            var left = $(this).attr('data-left');

            if (top && left) {
                val += ',' + top + ',' + left;

                $(this).val(val);
            }
        });
    });*/
});