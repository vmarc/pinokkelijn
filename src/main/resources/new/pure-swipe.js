/*!
* pure-swipe.js - v@version@
* Pure JavaScript swipe events
* https://github.com/john-doherty/pure-swipe
* @inspiration https://stackoverflow.com/questions/16348031/disable-scrolling-when-touch-moving-certain-element
* @author John Doherty <www.johndoherty.info>
* @license MIT
*/
(function(window, document) {
  'use strict';
  if ('initCustomEvent'in document.createEvent('CustomEvent')) {
    window.CustomEvent = function(event, params) {
      params = params || {
        bubbles: false,
        cancelable: false,
        detail: undefined
      };
      var evt = document.createEvent('CustomEvent');
      evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
      return evt;
    }
    ;
    window.CustomEvent.prototype = window.Event.prototype;
  }
  document.addEventListener('touchstart', handleTouchStart, false);
  document.addEventListener('touchmove', handleTouchMove, false);
  document.addEventListener('touchend', handleTouchEnd, false);
  var xDown = null;
  var yDown = null;
  var xDiff = null;
  var yDiff = null;
  var timeDown = null;
  var startEl = null;
  function handleTouchEnd(e) {
    if (startEl !== e.target)
      return;
    var swipeThreshold = parseInt(startEl.getAttribute('data-swipe-threshold') || '20', 10);
    var swipeTimeout = parseInt(startEl.getAttribute('data-swipe-timeout') || '500', 10);
    var timeDiff = Date.now() - timeDown;
    var eventType = '';
    if (Math.abs(xDiff) > Math.abs(yDiff)) {
      if (Math.abs(xDiff) > swipeThreshold && timeDiff < swipeTimeout) {
        if (xDiff > 0) {
          eventType = 'swiped-left';
        } else {
          eventType = 'swiped-right';
        }
      }
    } else {
      if (Math.abs(yDiff) > swipeThreshold && timeDiff < swipeTimeout) {
        if (yDiff > 0) {
          eventType = 'swiped-up';
        } else {
          eventType = 'swiped-down';
        }
      }
    }
    if (eventType !== '') {
      startEl.dispatchEvent(new CustomEvent(eventType,{
        bubbles: true,
        cancelable: true
      }));
      if (console && console.log)
        console.log(eventType + ' fired on ' + startEl.tagName);
    }
    xDown = null;
    yDown = null;
    timeDown = null;
  }
  function handleTouchStart(e) {
    if (e.target.getAttribute('data-swipe-ignore') === 'true')
      return;
    startEl = e.target;
    timeDown = Date.now();
    xDown = e.touches[0].clientX;
    yDown = e.touches[0].clientY;
    xDiff = 0;
    yDiff = 0;
  }
  function handleTouchMove(e) {
    if (!xDown || !yDown)
      return;
    var xUp = e.touches[0].clientX;
    var yUp = e.touches[0].clientY;
    xDiff = xDown - xUp;
    yDiff = yDown - yUp;
  }
}(this, document));
