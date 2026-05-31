-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS vector;

-- Users
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Workspaces
CREATE TABLE workspaces (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    is_private  BOOLEAN      NOT NULL DEFAULT true,
    owner_id    UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Workspace members
CREATE TABLE workspace_members (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workspace_id UUID        NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    user_id      UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role         VARCHAR(20) NOT NULL DEFAULT 'MEMBER' CHECK (role IN ('OWNER', 'MEMBER')),
    joined_at    TIMESTAMP   NOT NULL DEFAULT NOW(),
    UNIQUE (workspace_id, user_id)
);

-- Documents
CREATE TABLE documents (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workspace_id UUID         NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    uploaded_by  UUID         NOT NULL REFERENCES users(id),
    title        VARCHAR(255) NOT NULL,
    file_type    VARCHAR(50)  NOT NULL,
    storage_path TEXT         NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                 CHECK (status IN ('PENDING', 'PROCESSING', 'READY', 'FAILED')),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Document chunks + embeddings (pgvector)
CREATE TABLE document_chunks (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id  UUID    NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    chunk_index  INTEGER NOT NULL,
    content      TEXT    NOT NULL,
    embedding    vector(1536),   -- 1536 for OpenAI, 768 for Gemini
    created_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Index for fast cosine similarity search
CREATE INDEX idx_chunks_embedding
    ON document_chunks USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 100);

-- Chat sessions
CREATE TABLE chat_sessions (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workspace_id UUID         NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    user_id      UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL DEFAULT 'New chat',
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Messages
CREATE TABLE messages (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID        NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
    user_id    UUID        REFERENCES users(id),
    role       VARCHAR(10) NOT NULL CHECK (role IN ('user', 'assistant')),
    content    TEXT        NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- Chunk citations (which chunks were used to answer a message)
CREATE TABLE chunk_citations (
    id               UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    message_id       UUID  NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    chunk_id         UUID  NOT NULL REFERENCES document_chunks(id) ON DELETE CASCADE,
    similarity_score FLOAT NOT NULL
);

-- Indexes
CREATE INDEX idx_documents_workspace   ON documents(workspace_id);
CREATE INDEX idx_chunks_document       ON document_chunks(document_id);
CREATE INDEX idx_sessions_workspace    ON chat_sessions(workspace_id, user_id);
CREATE INDEX idx_messages_session      ON messages(session_id);
CREATE INDEX idx_citations_message     ON chunk_citations(message_id);