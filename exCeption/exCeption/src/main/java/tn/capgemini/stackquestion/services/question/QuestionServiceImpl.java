package tn.capgemini.stackquestion.services.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.dto.*;
import tn.capgemini.stackquestion.entities.*;
import tn.capgemini.stackquestion.entities.enums.VoteType;
import tn.capgemini.stackquestion.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements  QuestionService{
    // Number of questions per page
    public static final int SEARCH_RESULT_PER_PAGE = 5;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    QuestionVoteRepository questionVoteRepository;


    @Override
    public QuestionDTO addQuestion(QuestionDTO questionDto) {
        Optional<User> optionalUser = userRepository.findById(questionDto.getUserId());
        if (optionalUser.isPresent()){
            Optional<Question> existingQuestion = questionRepository.findByTitleIgnoreCase(questionDto.getTitle().trim());
            if (existingQuestion.isPresent()) {
                throw new RuntimeException("⚠️ A question with this title already exists.");
            }
            Question question = new Question();
            question.setTitle(questionDto.getTitle());
            question.setBody(questionDto.getBody());

            // Setting both:
            question.setTags(questionDto.getTags());  // saved in questions_tags table
            question.setTagsString(String.join(",", questionDto.getTags()));  // comma-separated tags in questions table

            question.setCreatedDate(new Date());
            question.setUser(optionalUser.get());
            question.setDepartement(questionDto.getDepartement());
            question.setProjet(questionDto.getProjet());

            Question createdQuestion = questionRepository.save(question);

            QuestionDTO createdQuestionDto = new QuestionDTO();
            createdQuestionDto.setId(createdQuestion.getId());
            createdQuestionDto.setTitle(createdQuestion.getTitle());
            createdQuestionDto.setTags(createdQuestion.getTags());
            createdQuestionDto.setUserId(createdQuestion.getUser().getUser_id());
            return createdQuestionDto;
        }
        return null;
    }


    @Override
    public AllQuestionResponseDto getAllQuestions(int pageNumber) {
        // Specify the sorting direction and the field to sort by
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));

        // Create a Pageable object with sorting
        Pageable paging = PageRequest.of(pageNumber, SEARCH_RESULT_PER_PAGE, sort);
        Page<Question> questionsPage =  questionRepository.findAll(paging);

        AllQuestionResponseDto allQuestionResponseDto = new AllQuestionResponseDto();

        allQuestionResponseDto.setQuestionDTOList(questionsPage.getContent().stream().map(Question::getQuestionDto).collect(Collectors.toList()));
        allQuestionResponseDto.setPageNumber(questionsPage.getPageable().getPageNumber());
        allQuestionResponseDto.setTotalPages(questionsPage.getTotalPages());
        return allQuestionResponseDto;
    }

    @Override
    public AllQuestionResponseDto getAllQuestions(int pageNumber, int size) {
        // Specify the sorting direction and the field to sort by
        Sort sort = Sort.by(Sort.Order.asc("voteCount"));

        // Create a Pageable object with sorting
        Pageable paging = PageRequest.of(pageNumber, size, sort);
        Page<Question> questionsPage =  questionRepository.findAll(paging);

        AllQuestionResponseDto allQuestionResponseDto = new AllQuestionResponseDto();

        allQuestionResponseDto.setQuestionDTOList(questionsPage.getContent().stream().map(Question::getQuestionDto).collect(Collectors.toList()));
        allQuestionResponseDto.setPageNumber(questionsPage.getPageable().getPageNumber());
        allQuestionResponseDto.setTotalPages(questionsPage.getTotalPages());
        return allQuestionResponseDto;
    }


    @Override
    public SingleQuestionDto getQuestionById(int userId, int questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if(optionalQuestion.isPresent()){
            SingleQuestionDto singleQuestionDto = new SingleQuestionDto();

            // vote check
            Question existingQuestion = optionalQuestion.get();
            Optional<QuestionVote> optionalQuestionVote = existingQuestion.getQuestionVoteList().stream().filter(
                    vote -> vote.getUser().getUser_id().equals(userId)
            ).findFirst();
            QuestionDTO questionDto = optionalQuestion.get().getQuestionDto();
            questionDto.setVoted(0);
            if(optionalQuestionVote.isPresent()){
                if(optionalQuestionVote.get().getVoteType().equals(VoteType.UPVOTE)){
                    questionDto.setVoted(1);
                }else{
                    questionDto.setVoted(-1);
                }
            }

            singleQuestionDto.setQuestionDTO(questionDto);

            // get the question's answers and set it to singleQuestionDto
            List<AnswerDto> answerDtoList = new ArrayList<>();
            List<Answer> answerList = answerRepository.findByQuestionId(questionId);
            for (Answer answer: answerList) {
                AnswerDto answerDto = answer.getAnswerDto();
                Image image = imageRepository.findByAnswer(answer);
                if (image != null) {
                    String base64 = Base64.getEncoder().encodeToString(image.getData());
                    answerDto.setImageUrl("data:" + image.getType() + ";base64," + base64);
                }

                answerDtoList.add(answerDto);
            }
            singleQuestionDto.setAnswerDtoList(answerDtoList);
            return singleQuestionDto;
        }
        return null;
    }

    @Override
    public AllQuestionResponseDto getAllQuestionsByUserId(int userId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, SEARCH_RESULT_PER_PAGE);
        Optional<User> user = userRepository.findById(userId);

        Page<Question> questionsPage =  questionRepository.findAllByUser(user.get(), paging);

        AllQuestionResponseDto allQuestionResponseDto = new AllQuestionResponseDto();

        allQuestionResponseDto.setQuestionDTOList(questionsPage.getContent().stream().map(Question::getQuestionDto).collect(Collectors.toList()));
        allQuestionResponseDto.setPageNumber(questionsPage.getPageable().getPageNumber());
        allQuestionResponseDto.setTotalPages(questionsPage.getTotalPages());
        return allQuestionResponseDto;
    }
    public QuestionVoteDto voteQuestion(int userId, int questionId, VoteType voteType) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if (optionalUser.isPresent() && optionalQuestion.isPresent()) {
            User user = optionalUser.get();
            Question question = optionalQuestion.get();

            Optional<QuestionVote> existingVoteOpt = questionVoteRepository.findByUserAndQuestion(user, question);


            QuestionVote vote;
            if (existingVoteOpt.isPresent()) {
                vote = existingVoteOpt.get();
                if (vote.getVoteType() == voteType) {
                    question.setVoteCount(question.getVoteCount() - 1);
                    questionRepository.save(question);
                    questionVoteRepository.delete(vote);
                    return null; // vote annulé
                } else {
                    vote.setVoteType(voteType);
                    question.setVoteCount(question.getVoteCount() + 1);
                    questionRepository.save(question);
                    vote = questionVoteRepository.save(vote);
                }
            } else {
                vote = new QuestionVote();
                vote.setUser(user);
                vote.setQuestion(question);
                vote.setVoteType(voteType);
                vote = questionVoteRepository.save(vote);
            }

            QuestionVoteDto dto = new QuestionVoteDto();
            dto.setId(vote.getId());
            dto.setUserId(userId);
            dto.setQuestionId(questionId);
            dto.setVoteType(vote.getVoteType());

            return dto;
        }

        return null;
    }
    @Override
    public Map<String, Integer> getQuestionVoteStats(int questionId) {
        int upvotes = questionVoteRepository.countByQuestionIdAndVoteType(questionId, VoteType.UPVOTE);
        int downvotes = questionVoteRepository.countByQuestionIdAndVoteType(questionId, VoteType.DOWNVOTE);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("upvotes", upvotes);
        stats.put("downvotes", downvotes);
        stats.put("score", upvotes - downvotes);

        return stats;
    }

    @Override
    public QuestionDTO updateQuestion(int questionId, QuestionDTO updatedQuestionDto) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Optional<User> optionalUser = userRepository.findById(updatedQuestionDto.getUserId());

        if (optionalQuestion.isEmpty()) {
            throw new RuntimeException("Question not found");
        }



        Question question = optionalQuestion.get();



        question.setTitle(updatedQuestionDto.getTitle());
        question.setBody(updatedQuestionDto.getBody());
        question.setDepartement(updatedQuestionDto.getDepartement());
        question.setProjet(updatedQuestionDto.getProjet());
        question.setTags(updatedQuestionDto.getTags());

        Question updated = questionRepository.save(question);
        return updated.getQuestionDto();
    }


    @Override
    public void deleteQuestion(int questionId, int userId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalQuestion.isEmpty()) {
            throw new RuntimeException("Question not found");
        }

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Question question = optionalQuestion.get();

        if (!question.getUser().getUser_id().equals(userId)) {
            throw new RuntimeException("⚠️ You are not allowed to delete this question.");
        }

        questionRepository.deleteById(questionId);
    }




}
