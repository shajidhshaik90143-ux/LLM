from accuracy import compute_accuracy
from coherence import compute_coherence
from perplexity import compute_perplexity

def evaluate_all(reference, candidate):
    return {
        "accuracy": compute_accuracy(reference, candidate),
        "coherence": compute_coherence(candidate),
        "perplexity": compute_perplexity(candidate)
    }
