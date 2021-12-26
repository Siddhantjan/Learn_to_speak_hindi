package com.personal.learn_to_speakhindi;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class FragmentPhraese extends Fragment {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    public FragmentPhraese() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "Aap kahaan ja rahe ho?",
                R.raw.pharses_where_are_you_going));
        words.add(new Word("What is your name?", "aapaka naam kya he?",
                R.raw.what_is_your_name));
        words.add(new Word("How are you", "aap kaise hain?",
                R.raw.how_are_you));
        words.add(new Word("I am Fine thanks, What about you?", "main theek hoon dhanyavaad aap kaise hai?",
                R.raw.i_m_fine_thanks));
        words.add(new Word("I am also good", "main bhee achchhee hoon",
                R.raw.imalso_good));
        words.add(new Word("I am not so good", "main theek nahin hoon",
                R.raw.im_not_so_good));
        words.add(new Word("How are you feeling?", "tumhe kaisa lag raha hai?",
                R.raw.how_you_feeling));
        words.add(new Word("I’m feeling good.", "main achchha mahasoos kar rahee hoon.",
                R.raw.i_m_feeling_good));
        words.add(new Word("Are you coming?", "kya aap aa rahe hain?",
                R.raw.are_you_coming));
        words.add(new Word("Yes, I’m coming.", "haan, aa rahee hoon.",
                R.raw.yaa_i_m_coming));
        words.add(new Word("I’m coming.", "aa rahee hoon.",
                R.raw.i_m_coming));
        words.add(new Word("Let’s go.", "chale",
                R.raw.let_go));
        words.add(new Word("Come here.", "yahaan aao",
                R.raw.come_here));
        words.add(new Word("I love you.", "mein aapase pyaar karatee hoon",
                R.raw.i_love_you));
        words.add(new Word("I loved you so much.", "main aapase bahut pyaar karatee thee",
                R.raw.i_loved_you));
        words.add(new Word("I love you forever.", "main tumhen hamesha pyaar karugee",
                R.raw.ilove_you_forever));
        words.add(new Word("Are you alright?", "tum theek to ho na?",
                R.raw.are_you_alright));
        words.add(new Word("Are you Okay?", "kya aap theek ho?",
                R.raw.are_you_okay));
        words.add(new Word("Yeah", "haan",
                R.raw.pharses_yeah));
        words.add(new Word("I'm happy.", "mein khush hoon.",
                R.raw.i_m_happy));
        words.add(new Word("Did you ate anything?", "kya tumane kuchh khaaya?",
                R.raw.did_you_ate));
        words.add(new Word("Yes, I ate dolma.", "haan, mainne dolama kha liya.",
                R.raw.yeah_i_ate));
        words.add(new Word("Do you love me?", "kya aap mujhase prem karate hain?",
                R.raw.do_you_love_me));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceID());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}