package com.bff.wespot.data.local.common.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.bff.wespot.data.local.ProfilePreference
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class ProfileSerializer @Inject constructor(): Serializer<ProfilePreference> {
    override val defaultValue: ProfilePreference = ProfilePreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProfilePreference =
        try {
            ProfilePreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: ProfilePreference, output: OutputStream) {
        t.writeTo(output)
    }
}
