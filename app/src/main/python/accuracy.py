def compute_accuracy(reference, candidate):
    if not reference or not candidate:
        return 0.0
    ref_words = reference.lower().split()
    cand_words = candidate.lower().split()
    if not ref_words or not cand_words:
        return 0.0
    common = len(set(ref_words).intersection(set(cand_words)))
    score = (common / float(max(len(ref_words), len(cand_words)))) * 100.0
    return min(max(score, 0.0), 100.0)
