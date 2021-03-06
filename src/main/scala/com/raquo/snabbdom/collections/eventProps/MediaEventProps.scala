package com.raquo.snabbdom.collections.eventProps

import com.raquo.snabbdom.GenericEventCallback
import com.raquo.snabbdom.collections.Builders
import com.raquo.snabbdom.nodes.{Node, NodeData}
import com.raquo.snabbdom.setters.EventProp

/**
  * Media Events - triggered by media like videos, images and audio. These apply to
  * all HTML elements, but they are most common in media elements, like <audio>,
  * <embed>, <img>, <object>, and <video>.
  */
trait MediaEventProps[N <: Node[N, D], D <: NodeData[N, D]] extends SharedEventProps[N, D] { self: Builders[N, D] =>

  /**
    * Script to be run on abort
    */
  lazy val onAbort: EventProp[GenericEventCallback, N, D] = eventProp("abort")

  /**
    * Script to be run when a file is ready to start playing (when it has buffered enough to begin)
    */
  lazy val onCanPlay: EventProp[GenericEventCallback, N, D] = eventProp("canplay")

  /**
    * Script to be run when a file can be played all the way to the end without pausing for buffering
    */
  lazy val onCanPlayThrough: EventProp[GenericEventCallback, N, D] = eventProp("canplaythrough")

  /**
    * Script to be run when the cue changes in a <track> element
    */
  lazy val onCueChange: EventProp[GenericEventCallback, N, D] = eventProp("cuechange")

  /**
    * Script to be run when the length of the media changes
    */
  lazy val onDurationChange: EventProp[GenericEventCallback, N, D] = eventProp("durationchange")

  /**
    * Script to be run when something bad happens and the file is suddenly unavailable (like unexpectedly disconnects)
    */
  lazy val onEmptied: EventProp[GenericEventCallback, N, D] = eventProp("emptied")

  /**
    * Script to be run when the media has reach the end (a useful event for messages like "thanks for listening")
    */
  lazy val onEnded: EventProp[GenericEventCallback, N, D] = eventProp("ended")

  /**
    * Script to be run when media data is loaded
    */
  lazy val onLoadedData: EventProp[GenericEventCallback, N, D] = eventProp("loadeddata")

  /**
    * Script to be run when meta data (like dimensions and duration) are loaded
    */
  lazy val onLoadedMetadata: EventProp[GenericEventCallback, N, D] = eventProp("loadedmetadata")

  /**
    * Script to be run just as the file begins to load before anything is actually loaded
    */
  lazy val onLoadStart: EventProp[GenericEventCallback, N, D] = eventProp("loadstart")

  /**
    * Script to be run when the media is paused either by the user or programmatically
    */
  lazy val onPause: EventProp[GenericEventCallback, N, D] = eventProp("pause")

  /**
    * Script to be run when the media is ready to start playing
    */
  lazy val onPlay: EventProp[GenericEventCallback, N, D] = eventProp("play")

  /**
    * Script to be run when the media actually has started playing
    */
  lazy val onPlaying: EventProp[GenericEventCallback, N, D] = eventProp("playing")

  /**
    * Script to be run when the browser is in the process of getting the media data
    */
  lazy val onProgress: EventProp[GenericEventCallback, N, D] = eventProp("progress")

  /**
    * Script to be run each time the playback rate changes (like when a user switches to a slow motion or fast forward mode)
    */
  lazy val onRateChange: EventProp[GenericEventCallback, N, D] = eventProp("ratechange")

  /**
    * Script to be run when the seeking attribute is set to false indicating that seeking has ended
    */
  lazy val onSeeked: EventProp[GenericEventCallback, N, D] = eventProp("seeked")

  /**
    * Script to be run when the seeking attribute is set to true indicating that seeking is active
    */
  lazy val onSeeking: EventProp[GenericEventCallback, N, D] = eventProp("seeking")

  /**
    * Script to be run when the browser is unable to fetch the media data for whatever reason
    */
  lazy val onStalled: EventProp[GenericEventCallback, N, D] = eventProp("stalled")

  /**
    * Script to be run when fetching the media data is stopped before it is completely loaded for whatever reason
    */
  lazy val onSuspend: EventProp[GenericEventCallback, N, D] = eventProp("suspend")

  /**
    * Script to be run when the playing position has changed (like when the user fast forwards to a different point in the media)
    */
  lazy val onTimeUpdate: EventProp[GenericEventCallback, N, D] = eventProp("timeupdate")

  /**
    * Script to be run each time the volume is changed which (includes setting the volume to "mute")
    */
  lazy val onVolumeChange: EventProp[GenericEventCallback, N, D] = eventProp("volumechange")

  /**
    * Script to be run when the media has paused but is expected to resume (like when the media pauses to buffer more data)
    */
  lazy val onWaiting: EventProp[GenericEventCallback, N, D] = eventProp("waiting")
}
