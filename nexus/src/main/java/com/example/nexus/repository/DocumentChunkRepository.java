package com.example.nexus.repository;

import com.example.nexus.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {

    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(UUID documentId);

    /**
     * Semantic search: find top-K chunks most similar to the query embedding
     * within a specific workspace, using cosine distance (<=>).
     *
     * Lower cosine distance = more similar.
     * The cast ::vector is required for pgvector to accept the parameter.
     */
    @Query(value = """
        SELECT dc.*
        FROM document_chunks dc
        JOIN documents d ON dc.document_id = d.id
        WHERE d.workspace_id = :workspaceId
          AND d.status = 'READY'
          AND dc.embedding IS NOT NULL
        ORDER BY dc.embedding <=> CAST(:embedding AS vector)
        LIMIT :topK
        """, nativeQuery = true)
    List<DocumentChunk> findTopKByWorkspace(
        @Param("workspaceId") UUID workspaceId,
        @Param("embedding")   String embedding,
        @Param("topK")        int topK
    );
}