package com.mihaineagu.data.api.v1.mappers;

import org.mapstruct.Named;

public interface GenericMapper {

    @Named("uriToId")
    default Long uriToId(String uri) {
        if (uri == null)
            return null;
        String [] tok = uri.split("/");

        return Long.valueOf(tok[tok.length-1]);
    }


    @Named("idToUri")
    default String idToUri(Long id) {
        return String.valueOf(id);
    }

}
