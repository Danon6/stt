export interface KnowledgeDTO {
    id: number;
    title: string;
    description: string;
    content: string;
    departement: string;
    projet: string;
    userId: number;
    userName?: string;
    imageBase64?: string;
    imageType?: string;
    fileBase64?: string;
    fileType?: string;
    createdDate?: string;
    voteCount?: number;
  }
  