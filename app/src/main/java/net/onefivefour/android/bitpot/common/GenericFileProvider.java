package net.onefivefour.android.bitpot.common;

import androidx.core.content.FileProvider;

/**
 * To allow external apps to files stored in our app, we need to
 * have our own FileProvider in Android 24+
 *
 * For more information see here: https://stackoverflow.com/questions/38200282
 */
public class GenericFileProvider extends FileProvider {}