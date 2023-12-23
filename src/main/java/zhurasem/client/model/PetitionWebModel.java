package zhurasem.client.model;

public class PetitionWebModel extends PetitionDto{
    public boolean someError;

    public PetitionWebModel() {}

    public PetitionWebModel(boolean someError, PetitionDto petitionDto) {
        super(petitionDto.getPid(),
                petitionDto.getTitle(),
                petitionDto.getText(),
                petitionDto.getGoal(),
                petitionDto.getDateFrom(),
                petitionDto.getAuthorPetitionId(),
                petitionDto.getCommentsIds(),
                petitionDto.getSignedUsersIds());
        this.someError = someError;
    }

    public boolean isSomeError() {
        return someError;
    }

    public void setSomeError(boolean someError) {
        this.someError = someError;
    }
}
