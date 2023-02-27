# sample-audio-player

A very basic Android application for displaying a list of audio entries and playing them.

Structurally quite simple, I opted not to go with a domain layer, keeping just the `data` and `app` modules (and `buildSrc`, of course). A single repository fetches the data, which goes to the UI layer without much massaging as it is quite straightward already.

Two ViewModels were implemented, one for each Screen. This was done more as a showcase than for actual functionality; the size of the application does not warrant this structure. Moreover, there ended up being some code duplication. Either dealing with ViewModel/Controllers for specific, shared Components or making it a single, centralized ViewModel would make more sense here.

`AudioDetailViewModel` implements the base `MediaPlayerController`, which is not the most straightforward structure, but could make sense in a bigger application.

Dealing with the MediaPlayer required a few changes and took up a surprising amount of time, as it's a pretty obscure API when mixed with more modern technologies. Internet's wisdom around it seems to be "just use ExoPlayer instead". Most notably, there is a bug with getting `mediaPlayer.currentPosition` that I couldn't quite crack. But hey, at least the UI isn't blocked anymore.

I maintained the template Navigation structure, only changing a few things around passing in data. Personally, I find Google's Compose Navigation library very limited in this aspect, and would recommend third party ones over it (as building custom navigation that deals well with Parseables, for example, is pretty time consuming).

Koin modules ended up being very simple, with just the State and Repository-related objects as singletons. No reason to make things more complex here, in my opinion.

There are a few comments here and there. Nothing stood out as requiring too much explanation, though.

### what (would be) left

Tests are the big one. I ended up not adding any because of the time constraints; could either have some unit tests or a functioning UI as MediaPlayer was a pain to get working properly.

I would, with more time, merge the ViewModels and work on adding unit tests where possible. Concerns are already decently separated with the actions/dispatch pattern. But, then again, given the app's low complexity, even these wouldn't be too numerous.

To fix that and add in more meat, I'd expand on the Repository aspect. Currently, the API we're fetching information from is very barebones. So I'd like to add in Room and create a proper domain layer. That way we'd have Repositories for more aggregated information, including properly persisting user inputs.

Which leads us to the final point: persistency. Nothing here is persisting correctly, considering ratings and favorites are just set withint `State`. Not even configuration changes are handled well at this point, which should definitely be fixed.

All in all, a pretty barebones structure/final result.

### video sample

(No audio, but the emulator is playing :))

https://user-images.githubusercontent.com/29930410/221484818-e5773239-b9df-4e33-a238-8179ed7b9c42.mp4
